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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;

/**
 *
 */
public class SignerTest {

	public static final String CONTEUDO = "saúde";

	/**
	 * Teste com conteúdo anexado
	 */
	@Test
	public void testSignAttached() {
		String certificadoFile = System.getenv("CERTIFICADO_TESTE");
		String certificadoSenha = System.getenv("CERTIFICADO_SENHA");

		assumeNotNull(certificadoFile, certificadoSenha);

		try {
			byte[] contentToSign = CONTEUDO.getBytes(StandardCharsets.UTF_8);
			char[] senha = certificadoSenha.toCharArray();

			KeyStore ks = KeyStore.getInstance("pkcs12");

			//InputStream is = this.getClass().getResourceAsStream(certificadoFile);
			InputStream is = new FileInputStream(certificadoFile);
			ks.load(is, senha);

			String alias = getAlias(ks);

			PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
			signer.setCertificates(ks.getCertificateChain(alias));

			// para token
			signer.setPrivateKey((PrivateKey) ks.getKey(alias, senha));
			signer.setSignaturePolicy(PolicyFactory.Policies.AD_RB_CADES_2_3);
			signer.setAlgorithm(SignerAlgorithmEnum.SHA512withRSA);

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
