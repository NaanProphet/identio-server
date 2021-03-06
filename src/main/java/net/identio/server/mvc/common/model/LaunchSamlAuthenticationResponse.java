/*
 * This file is part of Ident.io.
 *
 * Ident.io - A flexible authentication server
 * Copyright (c) 2017 Loeiz TANGUY
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.identio.server.mvc.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class LaunchSamlAuthenticationResponse {

    private String destinationUrl;
    private String errorStatus;
    private String binding;
    private String relayState;
    private String samlRequest;
    private String sigAlg;
    private String signature;

    public String getDestinationUrl() {
        return destinationUrl;
    }

    public LaunchSamlAuthenticationResponse setDestinationUrl(String destinationUrl) {
        this.destinationUrl = destinationUrl;
        return this;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    public LaunchSamlAuthenticationResponse setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
        return this;
    }

    public String getBinding() {
        return binding;
    }

    public LaunchSamlAuthenticationResponse setBinding(String binding) {
        this.binding = binding;
        return this;
    }

    public String getRelayState() {
        return relayState;
    }

    public LaunchSamlAuthenticationResponse setRelayState(String relayState) {
        this.relayState = relayState;
        return this;
    }

    public String getSamlRequest() {
        return samlRequest;
    }

    public LaunchSamlAuthenticationResponse setSamlRequest(String samlRequest) {
        this.samlRequest = samlRequest;
        return this;
    }

    public String getSigAlg() {
        return sigAlg;
    }

    public LaunchSamlAuthenticationResponse setSigAlg(String sigAlg) {
        this.sigAlg = sigAlg;
        return this;
    }

    public String getSignature() {
        return signature;
    }

    public LaunchSamlAuthenticationResponse setSignature(String signature) {
        this.signature = signature;
        return this;
    }
}
