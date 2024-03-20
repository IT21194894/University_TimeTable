//package com.timetable.universityTimetable;
//
//import org.springframework.security.authentication.AuthenticationManager;
//
//import com.timetable.universityTimetable.security.jwt.JwtUtils;
//
//public class AuthApi {
//	 private final AuthenticationManager authenticationManager;
//	    private final JwtUtils jwtTokenUtil;
//	    private final UserViewMapper userViewMapper;
//
//	    public AuthApi(AuthenticationManager authenticationManager,
//	                   JwtTokenUtil jwtTokenUtil,
//	                   UserViewMapper userViewMapper) {
//	        this.authenticationManager = authenticationManager;
//	        this.jwtTokenUtil = jwtTokenUtil;
//	        this.userViewMapper = userViewMapper;
//	    }
//
//	    @PostMapping("login")
//	    public ResponseEntity<UserView> login(@RequestBody @Valid AuthRequest request) {
//	        try {
//	            Authentication authenticate = authenticationManager
//	                .authenticate(
//	                    new UsernamePasswordAuthenticationToken(
//	                        request.getUsername(), request.getPassword()
//	                    )
//	                );
//
//	            User user = (User) authenticate.getPrincipal();
//
//	            return ResponseEntity.ok()
//	                .header(
//	                    HttpHeaders.AUTHORIZATION,
//	                    jwtTokenUtil.generateAccessToken(user)
//	                )
//	                .body(userViewMapper.toUserView(user));
//	        } catch (BadCredentialsException ex) {
//	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//	        }
//	    }
//}
