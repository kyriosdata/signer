package org.demoiselle.signer.core.extension;

import org.junit.Test;

import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;

import static junit.framework.TestCase.assertTrue;

public class PoliticaDeRestricao {

	@Test
	public void verificaSeRestricaoRemovida() throws NoSuchAlgorithmException {
		int maxKeySize = Cipher.getMaxAllowedKeyLength("AES");
		assertTrue(maxKeySize > 128);
	}
}
