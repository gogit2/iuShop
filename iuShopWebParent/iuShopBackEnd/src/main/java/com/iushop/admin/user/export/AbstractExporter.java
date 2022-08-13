package com.iushop.admin.user.export;

import com.iushop.common.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AbstractExporter {

    public void setResponseHeader(HttpServletResponse response, String extension, String contentType) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
        String timeStamp = dateFormatter.format(new Date());
        String fileName = "users_" + timeStamp + extension;

        response.setContentType(contentType);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }
}
