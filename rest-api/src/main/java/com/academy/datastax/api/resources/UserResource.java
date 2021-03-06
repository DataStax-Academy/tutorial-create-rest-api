package com.academy.datastax.api.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.academy.datastax.dao.CommentDseDao;
import com.academy.datastax.model.CommentByUser;
import com.academy.datastax.model.ResultPage;
import com.academy.datastax.model.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1/users/{useruuid}")
@Api(value = "Resource User",  description = "Operation on single User")
public class UserResource {
    
    /** Logger for that class. */
    private static Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
    
    @Autowired
    private CommentDseDao commentDao;
    
    @RequestMapping(value = "", method = GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get detailed information on a User", response = User.class)
    @ApiResponses(@ApiResponse(code = 200, message = "Get detailed information on a User"))
    public User userDetails(@ApiParam(name="useruuid", value="Unique identifier for a user", required=true ) 
    @PathVariable(value = "useruuid") String useruuid) {
       return new User("John", "Doe", UUID.fromString(useruuid));
    }
    
    @RequestMapping(value = "/comments", method = GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List comment for a specified user", response = ResultPage.class)
    @ApiResponses(
            @ApiResponse(code = 200, message = "Retrieve comments for a dedicated user")
    )
    public ResultPage< CommentByUser > getComments(
            @ApiParam(name="useruuid", value="Unique identifier for a user", required=true ) 
            @PathVariable(value = "useruuid") String useruuid,
            @ApiParam(name="pageSize", value="Requested page size, default is 10", required=false ) 
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @ApiParam(name="pageState", value="Use to retrieve next pages", required=false ) 
            @RequestParam("pageState") Optional<String> pageState) {
        LOGGER.info("Retrieving comments for a user ");
        return commentDao.readUserComments(UUID.fromString(useruuid), pageState, pageSize);
    }
    
}
