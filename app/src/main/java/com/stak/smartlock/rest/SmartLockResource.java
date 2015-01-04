package com.stak.smartlock.rest;

import com.stak.smartlock.rest.dto.CommandDTO;

import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * Created by gospo on 30.12.14.
 */
public interface SmartLockResource {
    @Post
    public boolean command(CommandDTO dto);
}
