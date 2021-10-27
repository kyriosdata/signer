/*
 * Demoiselle Framework
 * Copyright (C) 2021 SERPRO
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

package org.demoiselle.signer.core.extension;

import org.junit.Test;

import java.io.InputStream;

import static junit.framework.TestCase.*;

public class BasicCertificateTest {

	private BasicCertificate bc;

	public BasicCertificateTest() throws Exception {
		// Certificado de autoridade certificadora (AC)
		InputStream is = this.getClass().getResourceAsStream("/actest.cer");
		bc = new BasicCertificate(is);
	}

	@Test
	public void distinguishedNames() throws Exception {
		ICPBR_DN cidn = bc.getCertificateIssuerDN();
		assertEquals("Fake CA", cidn.getProperty("CN"));
		assertEquals("ICP-Brasil", cidn.getProperty("O"));
		assertEquals("BR", cidn.getProperty("C"));
	}

	@Test
	public void getNotBeforeDate() {
		String dataString = bc.getBeforeDate().toString();
		assertEquals("Tue Oct 26 19:15:39 BRT 2021", dataString);
	}

	@Test
	public void getNotAfterDate() {
		String dataString = bc.getAfterDate().toString();
		assertEquals("Thu Oct 19 19:15:39 BRT 2051", dataString);
	}

	@Test
	public void keyUsage() {
		ICPBRKeyUsage keyUsage = bc.getICPBRKeyUsage();
		assertTrue(keyUsage.isCRLSign());
		assertTrue(keyUsage.isDigitalSignature());
		assertTrue(keyUsage.isKeyCertSign());
		assertTrue(keyUsage.isKeyEncipherment());
		assertTrue(keyUsage.isDataEncipherment());
	}

	@Test
	public void subjectAlternativeName() {
		ICPBRSubjectAlternativeNames an = bc.getICPBRSubjectAlternativeNames();
		assertFalse(an.isCertificatePF());
		assertFalse(an.isCertificatePJ());
		assertFalse(an.isCertificateEquipment());
	}
}
