/*
 * Demoiselle Framework
 * Copyright (C) 2016 SERPRO
 * ----------------------------------------------------------------------------
 * This file is part of Demoiselle Framework.
 *
 * Demoiselle Framework is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3
 * along with this program; if not,  see <http://www.gnu.org/licenses/>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 * ----------------------------------------------------------------------------
 * Este arquivo é parte do Framework Demoiselle.
 *
 * O Framework Demoiselle é um software livre; você pode redistribuí-lo e/ou
 * modificá-lo dentro dos termos da GNU LGPL versão 3 como publicada pela Fundação
 * do Software Livre (FSF).
 *
 * Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA
 * GARANTIA; sem uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou
 * APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU/LGPL em português
 * para maiores detalhes.
 *
 * Você deve ter recebido uma cópia da GNU LGPL versão 3, sob o título
 * "LICENCA.txt", junto com esse programa. Se não, acesse <http://www.gnu.org/licenses/>
 * ou escreva para a Fundação do Software Livre (FSF) Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
 */

package org.demoiselle.signer.policy.impl.cades.pkcs7.impl;

import org.demoiselle.signer.policy.engine.factory.PolicyFactory;
import org.demoiselle.signer.policy.impl.cades.SignerAlgorithmEnum;
import org.demoiselle.signer.policy.impl.cades.factory.PKCS7Factory;
import org.demoiselle.signer.policy.impl.cades.pkcs7.PKCS7Signer;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 */
public class SignerTest {

	public static final String CONTEUDO = "saúde";
	public static final String SENHA = "changeit";

	/**
	 * Teste com conteúdo anexado
	 */
	@Test
	public void testSignAttached() {
		try {
			byte[] contentToSign = CONTEUDO.getBytes(StandardCharsets.UTF_8);
			char[] senha = SENHA.toCharArray();

			KeyStore ks = KeyStore.getInstance("JKS");
			InputStream is = this.getClass().getResourceAsStream("/icpbrasil.jks");
			ks.load(is, senha);

			String alias = getAlias(ks);

			PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
			signer.setCertificates(ks.getCertificateChain(alias));

			// para token
			signer.setPrivateKey((PrivateKey) ks.getKey(alias, null));

			// para arquivo
			// signer.setPrivateKey((PrivateKey) ks.getKey(alias, senha));
			// politica sem carimbo de tempo
			signer.setSignaturePolicy(PolicyFactory.Policies.AD_RB_CADES_2_3);

			// para mudar o algoritimo
			signer.setAlgorithm(SignerAlgorithmEnum.SHA512withRSA);
			String varSO = System.getProperty("os.name");
			if (varSO.contains("indows")) {
				signer.setAlgorithm(SignerAlgorithmEnum.SHA256withRSA);
			}

			// Com conteudo atachado
			byte[] p7s = signer.doAttachedSign(contentToSign);
			File file = new File("assinatura.p7s");
			FileOutputStream os = new FileOutputStream(file);
			os.write(p7s);
			os.flush();
			os.close();
			assertTrue(true);
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | IOException | CertificateException ex) {
			ex.printStackTrace();
			fail();
		}
	}

	private String getAlias(KeyStore ks) {
		Certificate[] certificates = null;
		String alias = "";
		Enumeration<String> e;
		try {
			e = ks.aliases();
			while (e.hasMoreElements()) {
				alias = e.nextElement();
				System.out.println("alias..............: " + alias);
				System.out.println("iskeyEntry" + ks.isKeyEntry(alias));
				System.out.println("containsAlias" + ks.containsAlias(alias));
				certificates = ks.getCertificateChain(alias);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return alias;
	}
}
