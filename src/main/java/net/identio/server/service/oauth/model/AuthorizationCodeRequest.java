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

package net.identio.server.service.oauth.model;

public class AuthorizationCodeRequest {

    private String code;
    private String redirectUri;
    private String codeVerifier;

    public String getCode() {
        return code;
    }

    public AuthorizationCodeRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public AuthorizationCodeRequest setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }

    public AuthorizationCodeRequest setCodeVerifier(String codeVerifier) {
        this.codeVerifier = codeVerifier;
        return this;
    }
}
