/**
 *  Copyright (C) 2010 Cloud.com, Inc.  All rights reserved.
 * 
 * This software is licensed under the GNU General Public License v3 or later.
 * 
 * It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.cloud.api.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloud.api.ApiConstants;
import com.cloud.api.ApiResponseHelper;
import com.cloud.api.BaseCmd;
import com.cloud.api.BaseListCmd;
import com.cloud.api.Implementation;
import com.cloud.api.Parameter;
import com.cloud.api.ServerApiException;
import com.cloud.api.response.ListResponse;
import com.cloud.api.response.StoragePoolResponse;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientAddressCapacityException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.PermissionDeniedException;
import com.cloud.storage.StoragePoolVO;

@Implementation(description="Lists storage pools.", responseObject=StoragePoolResponse.class)
public class ListStoragePoolsCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger(ListStoragePoolsCmd.class.getName());

    private static final String s_name = "liststoragepoolsresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.CLUSTER_ID, type=CommandType.LONG, description="list storage pools belongig to the specific cluster")
    private Long clusterId;

    @Parameter(name=ApiConstants.IP_ADDRESS, type=CommandType.STRING, description="the IP address for the storage pool")
    private String ipAddress;

    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, description="the name of the storage pool")
    private String storagePoolName;

    @Parameter(name=ApiConstants.PATH, type=CommandType.STRING, description="the storage pool path")
    private String path;

    @Parameter(name=ApiConstants.POD_ID, type=CommandType.LONG, description="the Pod ID for the storage pool")
    private Long podId;

    @Parameter(name=ApiConstants.ZONE_ID, type=CommandType.LONG, description="the Zone ID for the storage pool")
    private Long zoneId;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getClusterId() {
        return clusterId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getStoragePoolName() {
        return storagePoolName;
    }

    public String getPath() {
        return path;
    }

    public Long getPodId() {
        return podId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getName() {
        return s_name;
    }

    @Override
    public void execute() throws ServerApiException, InvalidParameterValueException, PermissionDeniedException, InsufficientAddressCapacityException, InsufficientCapacityException, ConcurrentOperationException{
        List<? extends StoragePoolVO> pools = BaseCmd._mgr.searchForStoragePools(this);
        ListResponse<StoragePoolResponse> response = new ListResponse<StoragePoolResponse>();
        List<StoragePoolResponse> poolResponses = new ArrayList<StoragePoolResponse>();
        for (StoragePoolVO pool : pools) {
            StoragePoolResponse poolResponse = ApiResponseHelper.createStoragePoolResponse(pool);
            poolResponse.setObjectName("storagepool");
            poolResponses.add(poolResponse);
        }

        response.setResponses(poolResponses);
        response.setResponseName(getName());
        this.setResponseObject(response);
    }
}
