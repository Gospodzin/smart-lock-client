package com.stak.smartlock.rest;

import com.stak.smartlock.rest.dto.ConfirmDTO;

import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * Created by gospo on 29.12.14.
 */
public interface RegisterResource {
    @Post
    public String confirm(ConfirmDTO dto);
}
