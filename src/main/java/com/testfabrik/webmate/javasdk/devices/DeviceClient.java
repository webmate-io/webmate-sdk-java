package com.testfabrik.webmate.javasdk.devices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.*;
import com.testfabrik.webmate.javasdk.commonutils.HttpHelpers;
import com.testfabrik.webmate.javasdk.packagemgmt.ImageId;
import com.testfabrik.webmate.javasdk.packagemgmt.ImagePool;
import com.testfabrik.webmate.javasdk.packagemgmt.ImageType;
import com.testfabrik.webmate.javasdk.packagemgmt.PackageId;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Facade to webmate's Device subsystem.
 */
public class DeviceClient {

    private WebmateAPISession session;
    private DeviceApiClient apiClient;

    private static final Logger LOG = LoggerFactory.getLogger(DeviceClient.class);

    private static class DeviceApiClient extends WebmateApiClient {

        private final static UriTemplate getDeviceIdsForProject = new UriTemplate("/projects/${projectId}/device/devices");

        private final static UriTemplate getDevice = new UriTemplate("/device/devices/${deviceId}");

        private final static UriTemplate requestDeviceByRequirementsForProject = new UriTemplate("/projects/${projectId}/device/devices");

        private final static UriTemplate synchronizeDevice = new UriTemplate("/device/devices/${deviceId}/sync");

        private final static UriTemplate releaseDevice = new UriTemplate("/device/devices/${deviceId}");

        private final static UriTemplate redeployDevice = new UriTemplate("/device/devices/${deviceId}/redeploy");

        private final static UriTemplate installAppOnDevice = new UriTemplate("/device/${deviceId}/appinstall/${packageId}");

        private final static UriTemplate uploadImage = new UriTemplate("/projects/${projectId}/images");

        private final static UriTemplate uploadImageToDevice = new UriTemplate("/device/${deviceId}/image/${imageId}");

        private final static UriTemplate setCameraSimulation = new UriTemplate("/device/devices/${deviceId}/capabilities");

        public DeviceApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
            super(authInfo, environment);
        }

        public DeviceApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder httpClientBuilder) {
            super(authInfo, environment, httpClientBuilder);
        }

        public Collection<DeviceId> getDeviceIdsForProject(ProjectId projectId) {
            ApiResponse response = sendGET(getDeviceIdsForProject, ImmutableMap.of("projectId", projectId.toString()));

            Optional<HttpResponse> optHttpResponse = response.getOptHttpResponse();

            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("Could not get device list. Got no response");
            }

            List<DeviceId> deviceIds = new ArrayList<>();
            try {
                String deviceIdsJson = EntityUtils.toString(optHttpResponse.get().getEntity());
                ObjectMapper mapper = JacksonMapper.getInstance();
                List<String> deviceIdList = mapper.readValue(deviceIdsJson, new TypeReference<List<String>>(){});
                for (String deviceIdStr : deviceIdList) {
                   deviceIds.add(new DeviceId(UUID.fromString(deviceIdStr)));
                }
            } catch (IOException e) {
                throw new WebmateApiClientException("Could not retrieve device list", e);
            }
            return deviceIds;
        }

        public DeviceDTO requestDeviceByRequirements(ProjectId projectId, DeviceRequest deviceRequest) {
            ObjectMapper mapper = JacksonMapper.getInstance();
            Optional<HttpResponse> optHttpResponse = sendPOST(requestDeviceByRequirementsForProject, ImmutableMap.of("projectId", projectId.toString()), mapper.valueToTree(deviceRequest)).getOptHttpResponse();

            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("Could not request device. Got no response");
            }

            return HttpHelpers.getObjectFromJsonEntity(optHttpResponse.get(), DeviceDTO.class);
        }

        public DeviceDTO getDevice(DeviceId deviceId) {
            Optional<HttpResponse> optHttpResponse = sendGET(getDevice, ImmutableMap.of("deviceId", deviceId.toString())).getOptHttpResponse();

            if (!optHttpResponse.isPresent()) {
                throw new WebmateApiClientException("Could not request device. Got no response");
            }

            return HttpHelpers.getObjectFromJsonEntity(optHttpResponse.get(), DeviceDTO.class);
        }

        public void synchronizeDevice(DeviceId deviceId) {
            sendPOST(synchronizeDevice, ImmutableMap.of("deviceId", deviceId.toString()));
        }

        public void releaseDevice(DeviceId deviceId) {
            sendDELETE(releaseDevice, ImmutableMap.of("deviceId", deviceId.toString()));
        }

        public void redeployDevice(DeviceId deviceId) {
            sendPOST(redeployDevice, ImmutableMap.of("deviceId", deviceId.toString()));
        }

        public void installAppOnDevice(DeviceId deviceId, PackageId appId, Boolean instrumented) {
            sendPOST(installAppOnDevice, ImmutableMap.of("deviceId", deviceId.toString(), "packageId", appId.toString()), "wait=true&instrumented=" + instrumented.toString());
        }

        public ImageId uploadImage(ProjectId projectId, byte[] image, String imageName, ImageType imageType) {
            Optional<String> contentType = Optional.of("image/" + imageType.getValue());
            List<NameValuePair> queryParams = new ArrayList<>();
            queryParams.add(new BasicNameValuePair("name", imageName));

            Optional<HttpResponse> r = sendPOST(uploadImage, ImmutableMap.of("projectId", projectId.toString()), image, contentType, queryParams).getOptHttpResponse();

            if (!r.isPresent()) {
                throw new WebmateApiClientException("Could not upload image. Got no response");
            }
            try {
                return new ImageId(readUUIDFromResponse(r.get()));
            } catch (IOException e) {
                throw new WebmateApiClientException("Error sending image data: " + e.getMessage(), e);
            }
        }

        public void uploadImageToDevice(DeviceId deviceId, ImageId imageId) {
            sendPOST(uploadImageToDevice, ImmutableMap.of("deviceId", deviceId.toString(), "imageId", imageId.toString()));
        }

        public ImageId uploadImageToDevice(ProjectId projectId, byte[] image, String imageName, ImageType imageType, DeviceId deviceId) {
            ImageId imageId = uploadImage(projectId, image, imageName, imageType);
            uploadImageToDevice(deviceId, imageId);
            return imageId;
        }

        public void setCameraSimulation(DeviceId deviceId, ImageId imageId, boolean simulate, ImagePool imagePool) {
            if (!imagePool.contains(imageId)) {
                throw new IllegalArgumentException("The given image pool must contain the passed image id.");
            }
            ObjectNode simulateCameraNode = JsonNodeFactory.instance.objectNode();
            simulateCameraNode.put("enabled", simulate);
            String selectedImageId = imageId == null ? null : imageId.toString();
            simulateCameraNode.put("selectedImage", selectedImageId);
            Map<String, Object> params = ImmutableMap.of(CapabilityConstants.SIMULATE_CAMERA, simulateCameraNode, CapabilityConstants.MEDIA_SETTINGS, imagePool.toJson());
            ObjectMapper mapper = new ObjectMapper();
            sendPOST(setCameraSimulation, ImmutableMap.of("deviceId", deviceId.toString()), mapper.valueToTree(params));
        }

    }

    /**
     * Creates a DeviceClient based on a WebmateApiSession.
     *
     * @param session The WebmateApiSession the DeviceClient is supposed to be based on.
     */
    public DeviceClient(WebmateAPISession session) {
        this.session = session;
        this.apiClient = new DeviceApiClient(session.authInfo, session.environment);
    }

    /**
     * Creates a DeviceClient based on a WebmateApiSession and a custom HttpClientBuilder.
     *
     * @param session The WebmateApiSession the DeviceClient is supposed to be based on.
     * @param httpClientBuilder The HttpClientBuilder that is used for building the underlying connection.
     */
    public DeviceClient(WebmateAPISession session,  HttpClientBuilder httpClientBuilder) {
        this.session = session;
        this.apiClient = new DeviceApiClient(session.authInfo, session.environment, httpClientBuilder);
    }

    /**
     * Get all Device ids for a project.
     *
     * @param projectId Id of Project (as found in dashboard), for which devices should be retrieved.
     * @return Collection of device ids.
     */
    public Collection<DeviceId> getDeviceIdsForProject(ProjectId projectId) {
        return this.apiClient.getDeviceIdsForProject(projectId);
    }


    /**
     * Get Information about running device.
     *
     * @param deviceId Id of running device (as found in device details on device overview page)
     * @return information about device
     */
    public DeviceDTO getDeviceInfo(DeviceId deviceId) {
        return this.apiClient.getDevice(deviceId);
    }

    /**
     * Request a device deployment by the specified device request.
     *
     * @param projectId Id of Project (as found in dashboard), for which devices should be retrieved.
     * @param deviceRequest Contains the defined device properties.
     */
    public DeviceDTO requestDeviceByRequirements(ProjectId projectId, DeviceRequest deviceRequest) {
        return this.apiClient.requestDeviceByRequirements(projectId, deviceRequest);
    }

    /**
     * Request a device deployment by the specified device request in the currently active project.
     *
     * @param deviceRequest Contains the defined device properties.
     */
    public DeviceDTO requestDeviceByRequirements(DeviceRequest deviceRequest) {
        Optional<ProjectId> projectId = this.session.getProjectId();
        if (!projectId.isPresent()) {
            throw new WebmateApiClientException("No project id associated with webmate session.");
        }
        return this.requestDeviceByRequirements(projectId.get(), deviceRequest);
    }

    /**
     * Synchronize webmate with device. (Usually not necessary)
     *
     * @param deviceId DeviceId of device. Can be found in "Details" dialog of an item in webmate device overview.
     */
    public void synchronizeDevice(DeviceId deviceId) {
        this.apiClient.synchronizeDevice(deviceId);
    }

    /**
     * Release device. The device will not be deployed afterwards.
     *
     * @param deviceId DeviceId of device. Can be found in "Details" dialog of an item in webmate device overview.
     */
    public void releaseDevice(DeviceId deviceId) {
        this.apiClient.releaseDevice(deviceId);
    }

    /**
     * Redeploy device. The device will be released and redeployed with the same properties as before.
     *
     * @param deviceId DeviceId of device. Can be found in "Details" dialog of an item in webmate device overview.
     */
    public void redeployDevice(DeviceId deviceId) {
        this.apiClient.redeployDevice(deviceId);
    }

    /**
     * Install the app wit the given Id on a device. If instrumented is set to true, the instrumented version will be
     * used if available.
     *
     * @param deviceId DeviceId of device. Can be found in "Details" dialog of an item in webmate device overview.
     * @param appId Id of app to be installed. Can be found in App management of the webmate device overview.
     * @param instrumented If true, the instrumented version of the app will be installed, if available.
     */
    public void installAppOnDevice(DeviceId deviceId, PackageId appId, Boolean instrumented) {
        this.apiClient.installAppOnDevice(deviceId, appId, instrumented);
    }

    /**
     * Install the app wit the given Id on a device. The non-instrumented version of the App will be installed.
     *
     * @param deviceId DeviceId of device. Can be found in "Details" dialog of an item in webmate device overview.
     * @param appId Id of app to be installed. Can be found in App management of the webmate device overview.
     */
    public void installAppOnDevice(DeviceId deviceId, PackageId appId) {
        this.apiClient.installAppOnDevice(deviceId, appId, false);
    }

    /**
     * Uploads am image to webmate. The image is defined by the given byte array and specified by the imageType. The
     * uploaded image can be referenced by the returned image id.
     *
     * @param projectId Id of Project (as found in dashboard), for which devices should be retrieved.
     * @param image Image data.
     * @param imageName Desired name of the image.
     * @param imageType Image format type.
     * @return Id of the uploaded image.
     */
    public ImageId uploadImage(ProjectId projectId, byte[] image, String imageName, ImageType imageType) {
        return this.apiClient.uploadImage(projectId, image, imageName, imageType);
    }

    /**
     * Uploads an image to a device. The image is identified by the given image id. The image must have been uploaded
     * to webmate before
     *
     * @param deviceId DeviceId of device. Can be found in "Details" dialog of an item in webmate device overview.
     * @param imageId Id of the image to be pushed to device.
     */
    public void uploadImageToDevice(DeviceId deviceId, ImageId imageId) {
        this.apiClient.uploadImageToDevice(deviceId, imageId);
    }

    /**
     * Uploads an image to webmate and pushes it to a device. The image is defined by the given byte array and specified
     * by the imageType. The uploaded image can be referenced by the returned image id.
     *
     * @param projectId Id of Project (as found in dashboard), for which devices should be retrieved.
     * @param image Image data.
     * @param imageName Desired name of the image.
     * @param imageType Image format type.
     * @param deviceId DeviceId of device. Can be found in "Details" dialog of an item in webmate device overview.
     * @return Id of the uploaded image.
     */
    public ImageId uploadImageToDevice(ProjectId projectId, byte[] image, String imageName, ImageType imageType,
                                       DeviceId deviceId) {
        return this.apiClient.uploadImageToDevice(projectId, image, imageName, imageType, deviceId);
    }

    /**
     * Configure the camera simulation to use the given selectedImageId. The simulation can be enabled or disabled. The
     * imagePool includes all images that should be pushed to the device. Note that the imagePool must contain the
     * given selectedImageId.
     *
     * @param deviceId DeviceId of device. Can be found in "Details" dialog of an item in webmate device overview.
     * @param selectedImageId Image id of an already uploaded image. The parameter sets the image that is to be used for
     *                        the camera simulation. It can also be null to reset the selected image.
     * @param simulate Disables or enables the camera simulation.
     * @param imagePool All images that are supposed to be pushed to the device. The parameter must contain the given
     *                  selectedImageId.
     */
    public void setCameraSimulation(DeviceId deviceId, ImageId selectedImageId, boolean simulate, ImagePool imagePool) {
        this.apiClient.setCameraSimulation(deviceId, selectedImageId, simulate, imagePool);
    }

    /**
     * Configure the camera simulation to use the given selectedImageId. The simulation can be enabled or disabled.
     *
     * @param deviceId DeviceId of device. Can be found in "Details" dialog of an item in webmate device overview.
     * @param selectedImageId Image id of an already uploaded image. The parameter sets the image that is to be used for
     *                        the camera simulation. It can also be null to reset the selected image.
     * @param simulate Disables or enables the camera simulation.
     */
    public void setCameraSimulation(DeviceId deviceId, ImageId selectedImageId, boolean simulate) {
        setCameraSimulation(deviceId, selectedImageId, simulate, new ImagePool(selectedImageId));
    }

    /**
     * Uploads an image to webmate, pushes it to a device and configures the camera simulation to use the image. The
     * image is defined by the given byte array and specified by the imageType. The uploaded image can be referenced by
     * the returned image id. The camera simulation will be enabled.
     *
     * @param projectId Id of Project (as found in dashboard), for which devices should be retrieved.
     * @param image Image data.
     * @param imageName Desired name of the image.
     * @param imageType Image format type.
     * @param deviceId DeviceId of device. Can be found in "Details" dialog of an item in webmate device overview.
     * @return Id of the uploaded image.
     */
    public ImageId uploadImageToDeviceAndSetForCameraSimulation(ProjectId projectId, byte[] image, String imageName,
                                                                ImageType imageType, DeviceId deviceId) {
        ImageId imageId = uploadImageToDevice(projectId, image, imageName, imageType, deviceId);
        setCameraSimulation(deviceId, imageId, true);
        return imageId;
    }

}
