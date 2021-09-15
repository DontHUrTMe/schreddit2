package com.gyuri.reddit.schreddit

import hu.gerviba.authsch.AuthSchAPI
import hu.gerviba.authsch.response.ProfileDataResponse
import hu.gerviba.authsch.struct.Scope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*
import javax.servlet.http.HttpServletRequest

const val USER_SESSION_ATTRIBUTE_NAME = "user_id"
const val USER_ENTITY_DTO_SESSION_ATTRIBUTE_NAME = "user"
const val CIRCLE_OWNERSHIP_SESSION_ATTRIBUTE_NAME = "circles"

@Controller
open class LoginController {

    @Autowired
    private lateinit var authSch: AuthSchAPI

    @Value("\${schpincer.sysadmins:}")
    private lateinit var systemAdmins: String

    @GetMapping("/loggedin")
    fun loggedIn(@RequestParam code: String, @RequestParam state: String, request: HttpServletRequest): String {
        if (buildUniqueState(request) != state)
            return "index?invalid-state"

        var auth: Authentication? = null
        try {
            val response = authSch.validateAuthentication(code)
            val profile = authSch.getProfile(response.accessToken)
            //val user: UserEntity
            val authorities: MutableList<GrantedAuthority> = ArrayList()
            authorities.add(SimpleGrantedAuthority("ROLE_${Role.USER.name}"))
            auth = UsernamePasswordAuthenticationToken(code, state, authorities)

            //request.session.setAttribute(USER_SESSION_ATTRIBUTE_NAME, user.uid)
            //request.session.setAttribute(USER_ENTITY_DTO_SESSION_ATTRIBUTE_NAME, user)
            SecurityContextHolder.getContext().authentication = auth
        } catch (e: Exception) {
            auth?.isAuthenticated = false
            e.printStackTrace()
        }
        return if (auth != null && auth.isAuthenticated) "redirect:/approve" else "redirect:/?error"
    }

    @GetMapping("/login")
    fun items(request: HttpServletRequest): String {
        return "redirect:" + authSch.generateLoginUrl(buildUniqueState(request),
                Scope.BASIC, Scope.GIVEN_NAME, Scope.SURNAME, Scope.MAIL)
    }

    fun buildUniqueState(request: HttpServletRequest): String {
        return (request.session.id
                + request.localAddr
                + request.localPort).sha256()
    }

    @GetMapping("/logout")
    fun logout(request: HttpServletRequest): String {
        request.getSession(false)
        SecurityContextHolder.clearContext()
        val session = request.getSession(false)
        session?.invalidate()
        for (cookie in request.cookies) {
            cookie.maxAge = 0
        }

        request.removeAttribute(USER_SESSION_ATTRIBUTE_NAME)
        request.removeAttribute(USER_ENTITY_DTO_SESSION_ATTRIBUTE_NAME)
        request.removeAttribute(CIRCLE_OWNERSHIP_SESSION_ATTRIBUTE_NAME)
        request.session.removeAttribute(USER_SESSION_ATTRIBUTE_NAME)
        request.session.removeAttribute(USER_ENTITY_DTO_SESSION_ATTRIBUTE_NAME)
        request.session.removeAttribute(CIRCLE_OWNERSHIP_SESSION_ATTRIBUTE_NAME)
        request.changeSessionId()
        //return "redirect:/?logged-out"
        return "redirect:/"
    }

}