package se.vgregion.activation.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.ws.rs.core.UriInfo;

import se.vgregion.activation.api.ActivationAccountDTO;
import se.vgregion.activation.domain.ActivationAccount;

public final class DTOAssembler {
    static ActivationAccountDTO toDTO(final ActivationAccount account, UriInfo ui) throws MalformedURLException {
        return toDTO(account, ui, "");
    }

    static ActivationAccountDTO toDTO(final ActivationAccount account, UriInfo ui, String pathSuffix)
            throws MalformedURLException {
        ActivationAccountDTO dto = null;
        if (account != null) {
            String base = ui.getBaseUri().toString();
            if (!base.endsWith("/")) base += "/";

            URL link = new URL(base + ui.getPath() + pathSuffix);
            dto = new ActivationAccountDTO(account.getVgrId(), account.getActivationCode().getValue(), link);
        }
        return dto;
    }

    static Collection<ActivationAccountDTO> toDTOCollection(final Collection<ActivationAccount> allAccounts,
            UriInfo ui) throws MalformedURLException {
        Collection<ActivationAccountDTO> dtoCollection = Collections.emptySet();
        if (allAccounts != null) {
            dtoCollection = new HashSet<ActivationAccountDTO>(allAccounts.size());
            for (ActivationAccount account : allAccounts) {
                dtoCollection.add(toDTO(account, ui, account.getId().getValue()));
            }
        }
        return dtoCollection;
    }
}
