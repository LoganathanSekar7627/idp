/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.idp.controllers;

import io.mosip.idp.core.exception.IdPException;
import io.mosip.idp.core.spi.OpenIdConnectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/oidc")
public class OpenIdConnectController {

    @Autowired
    private OpenIdConnectService openIdConnectService;

    /**
     * 1. The UserInfo Endpoint MUST accept Access Tokens
     * 2. The UserInfo Endpoint SHOULD support the use of Cross Origin Resource Sharing (CORS) [CORS] and or other methods
     * as appropriate to enable Javascript Clients to access the endpoint.
     * @param bearerToken
     * @return
     * @throws IdPException
     */
    @GetMapping("/userinfo")
    @CrossOrigin
    public String getUserInfo(@RequestHeader("Authorization") String bearerToken) throws IdPException {
        return openIdConnectService.getUserInfo(bearerToken);
    }

    @GetMapping("/.well-known/openid-configuration")
    public Map<String, Object> getDiscoveryEndpoints() {
        return openIdConnectService.getOpenIdConfiguration();
    }
}
