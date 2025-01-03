package com.example.ecommerce.Service.Impl;

import com.example.ecommerce.Service.CommonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Service
public class CommonServiceImpl implements CommonService {
    @Override
    public void removeSessionMsg() {
        HttpServletRequest request=((ServletRequestAttributes)((RequestContextHolder.getRequestAttributes()))).getRequest();
        HttpSession session=request.getSession();
        session.removeAttribute("succMsg");
        session.removeAttribute("errorMsg");
    }
}
