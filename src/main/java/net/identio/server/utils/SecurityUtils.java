/*
 This file is part of Ident.io

 Ident.io - A flexible authentication server
 Copyright (C) Loeiz TANGUY

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.identio.server.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SecurityUtils {

	public static final String escapeDN(String name) {
		StringBuilder sb = new StringBuilder();

		if ((name.length() > 0) && ((name.charAt(0) == ' ') || (name.charAt(0) == '#'))) {
			sb.append('\\'); // add the leading backslash if needed
		}

		for (int i = 0; i < name.length(); i++) {
			char curChar = name.charAt(i);
			switch (curChar) {
			case '\\':
				sb.append("\\\\");
				break;
			case ',':
				sb.append("\\,");
				break;
			case '+':
				sb.append("\\+");
				break;
			case '"':
				sb.append("\\\"");
				break;
			case '<':
				sb.append("\\<");
				break;
			case '>':
				sb.append("\\>");
				break;
			case ';':
				sb.append("\\;");
				break;
			default:
				sb.append(curChar);
				break;
			}
		}

		if ((name.length() > 1) && (name.charAt(name.length() - 1) == ' ')) {
			sb.insert(sb.length() - 1, '\\'); // add the trailing backslash if
												// needed
		}

		return sb.toString();
	}

	public static final String escapeLDAPSearchFilter(String filter) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < filter.length(); i++) {
			char curChar = filter.charAt(i);
			switch (curChar) {
			case '\\':
				sb.append("\\5c");
				break;
			case '*':
				sb.append("\\2a");
				break;
			case '(':
				sb.append("\\28");
				break;
			case ')':
				sb.append("\\29");
				break;
			case '\u0000':
				sb.append("\\00");
				break;
			default:
				sb.append(curChar);
				break;
			}
		}
		return sb.toString();
	}


	public static final void addCertificateToKeyStore(KeyStore ks, X509Certificate cert, String alias)
			throws FileNotFoundException, IOException, CertificateException, KeyStoreException {

		ks.setCertificateEntry(alias, cert);
	}

	public static final X509Certificate parseCertificate(String path)
			throws FileNotFoundException, IOException, CertificateException {

		try (FileInputStream fis = new FileInputStream(path); BufferedInputStream bis = new BufferedInputStream(fis)) {

			CertificateFactory cf = CertificateFactory.getInstance("X.509");

			return (X509Certificate) cf.generateCertificate(bis);

		}

	}

}
