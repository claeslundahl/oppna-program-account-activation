/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.activation.controllers;

import static se.vgregion.activation.controllers.DTOAssembler.*;

import java.net.MalformedURLException;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;

import se.vgregion.account.services.AccountService;
import se.vgregion.activation.api.ActivationAccountDTO;
import se.vgregion.activation.domain.ActivationAccount;
import se.vgregion.activation.domain.ActivationAccountStatus;
import se.vgregion.activation.domain.ActivationCode;

@Path("/activation-codes")
@Produces("application/json")
public class ActivationCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivationCodeService.class);

    @Context
    private UriInfo uriInfo;

    @Value("${pathToActivation}")
    String pathToActivation;

    private final AccountService accountService;

    @Autowired
    public ActivationCodeService(AccountService service) {
        this.accountService = service;
    }

    @GET
    public Collection<ActivationAccountDTO> getAllActivationCodes() {
        try {
            return toDTOCollection(accountService.getAllValidAccounts(), uriInfo);
        } catch (MalformedURLException e) {
            throw new WebApplicationException(500);
        }
    }

    @POST
    public String createActivationCode(ActivationAccountDTO account) {
        try {
            return accountService.createAccount(account.getVgrId(), account.getCustomUrl().toString(), account.getCustomMessage()).toString();
        } catch (DataAccessException e) {
            LOGGER.warn("Failed to create account.");
            throw new WebApplicationException(e, 500);
        }
    }

    @GET
    @Path("/{id}")
    public ActivationAccountDTO getActivationCode(@PathParam("id") ActivationCode id) {
        ActivationAccount account = accountService.getAccount(id);
        if (account == null) {
            throw new WebApplicationException(404);
        }
        try {
            return toDTO(account, uriInfo);
        } catch (MalformedURLException e) {
            throw new WebApplicationException(500);
        }
    }

    @GET
    @Path("/vgrid/{vgrid}")
    public ActivationAccountDTO getActivationCode(@PathParam("vgrid") String vgrId) {
        if (StringUtils.isBlank(vgrId)) {
            throw new WebApplicationException(404);
        }
        try {
            for (ActivationAccount account : accountService.getAllValidAccounts()) {
                if (vgrId.equals(account.getVgrId())) {
                    return toDTO(account, uriInfo);
                }
            }
        } catch (MalformedURLException e) {
            throw new WebApplicationException(500);
        }
        throw new WebApplicationException(404);
    }

    @PUT
    @Path("/{id}/inactivate")
    public void inactivateActivationCode(@PathParam("id") ActivationCode id) {
        ActivationAccount account = accountService.getAccount(id);
        if (account == null) {
            throw new WebApplicationException(404);
        }
        accountService.inactivate(account);
    }

    @PUT
    @Path("/{id}/reactivate")
    public void reactivateActivationCode(@PathParam("id") ActivationCode id) {
        ActivationAccount account = accountService.getAccount(id);
        if (account == null) {
            throw new WebApplicationException(404);
        }
        accountService.reactivate(account);
    }

    @GET
    @Path("/path/activation-code/{id}")
    @Produces("text/plain")
    public String getActivationFormUrl(@PathParam("id") ActivationCode id) {
        ActivationAccount account = accountService.getAccount(id);
        if (account == null) {
            throw new WebApplicationException(404);
        } else if (account.currentStatus() != ActivationAccountStatus.OK) {
            throw new WebApplicationException(400); //400 = BAD_REQUEST
        }
        return pathToActivation + "&activationCode=" + id.getValue();
    }

}
