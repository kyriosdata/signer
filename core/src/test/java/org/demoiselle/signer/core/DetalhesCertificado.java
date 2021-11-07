package org.demoiselle.signer.core;

import org.demoiselle.signer.core.extension.ICPBrasilExtension;
import org.demoiselle.signer.core.extension.ICPBrasilExtensionType;

class DetalhesCertificado {

	public DetalhesCertificado() {}

	@ICPBrasilExtension(type = ICPBrasilExtensionType.NAME)
	public String name;

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "DetalhesCertificado{" +
			", nome='" + name + '\'' +
			'}';
	}
}
