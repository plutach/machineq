/*     */ package kr.co.corners.common;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum HttpStatus
/*     */ {
/*  17 */   CONTINUE(100, "Continue"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  22 */   SWITCHING_PROTOCOLS(101, "Switching Protocols"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  27 */   PROCESSING(102, "Processing"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  33 */   CHECKPOINT(103, "Checkpoint"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  41 */   OK(200, "OK"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  46 */   CREATED(201, "Created"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  51 */   ACCEPTED(202, "Accepted"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  56 */   NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  61 */   NO_CONTENT(204, "No Content"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  66 */   RESET_CONTENT(205, "Reset Content"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  71 */   PARTIAL_CONTENT(206, "Partial Content"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  76 */   MULTI_STATUS(207, "Multi-Status"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  81 */   ALREADY_REPORTED(208, "Already Reported"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  86 */   IM_USED(226, "IM Used"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */   MULTIPLE_CHOICES(300, "Multiple Choices"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  99 */   MOVED_PERMANENTLY(301, "Moved Permanently"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 104 */   FOUND(302, "Found"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */   MOVED_TEMPORARILY(302, "Moved Temporarily"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */   SEE_OTHER(303, "See Other"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 121 */   NOT_MODIFIED(304, "Not Modified"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */   USE_PROXY(305, "Use Proxy"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */   TEMPORARY_REDIRECT(307, "Temporary Redirect"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 138 */   PERMANENT_REDIRECT(308, "Permanent Redirect"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 146 */   BAD_REQUEST(400, "Bad Request"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 151 */   UNAUTHORIZED(401, "Unauthorized"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 156 */   PAYMENT_REQUIRED(402, "Payment Required"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 161 */   FORBIDDEN(403, "Forbidden"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 166 */   NOT_FOUND(404, "Not Found"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 171 */   METHOD_NOT_ALLOWED(405, "Method Not Allowed"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 176 */   NOT_ACCEPTABLE(406, "Not Acceptable"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 181 */   PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 186 */   REQUEST_TIMEOUT(408, "Request Timeout"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 191 */   CONFLICT(409, "Conflict"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 196 */   GONE(410, "Gone"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 201 */   LENGTH_REQUIRED(411, "Length Required"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 206 */   PRECONDITION_FAILED(412, "Precondition Failed"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 212 */   PAYLOAD_TOO_LARGE(413, "Payload Too Large"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 218 */   REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 225 */   URI_TOO_LONG(414, "URI Too Long"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 231 */   REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 237 */   UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 242 */   REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 247 */   EXPECTATION_FAILED(417, "Expectation Failed"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 252 */   I_AM_A_TEAPOT(418, "I'm a teapot"), 
/*     */   
/*     */ 
/*     */ 
/* 256 */   INSUFFICIENT_SPACE_ON_RESOURCE(419, "Insufficient Space On Resource"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 261 */   METHOD_FAILURE(420, "Method Failure"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 266 */   DESTINATION_LOCKED(421, "Destination Locked"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 272 */   UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 277 */   LOCKED(423, "Locked"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 282 */   FAILED_DEPENDENCY(424, "Failed Dependency"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 287 */   UPGRADE_REQUIRED(426, "Upgrade Required"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 292 */   PRECONDITION_REQUIRED(428, "Precondition Required"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 297 */   TOO_MANY_REQUESTS(429, "Too Many Requests"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 302 */   REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 310 */   INTERNAL_SERVER_ERROR(500, "Internal Server Error"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 315 */   NOT_IMPLEMENTED(501, "Not Implemented"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 320 */   BAD_GATEWAY(502, "Bad Gateway"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 325 */   SERVICE_UNAVAILABLE(503, "Service Unavailable"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 330 */   GATEWAY_TIMEOUT(504, "Gateway Timeout"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 335 */   HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 340 */   VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 345 */   INSUFFICIENT_STORAGE(507, "Insufficient Storage"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 350 */   LOOP_DETECTED(508, "Loop Detected"), 
/*     */   
/*     */ 
/*     */ 
/* 354 */   BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 359 */   NOT_EXTENDED(510, "Not Extended"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 364 */   NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");
/*     */   
/*     */   private final int value;
/*     */   private final String reasonPhrase;
/*     */   
/*     */   private HttpStatus(int value, String reasonPhrase)
/*     */   {
/* 371 */     this.value = value;
/* 372 */     this.reasonPhrase = reasonPhrase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int value()
/*     */   {
/* 379 */     return this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getReasonPhrase()
/*     */   {
/* 386 */     return this.reasonPhrase;
/*     */   }
/*     */ }


/* Location:              F:\Nb-IoT\ciotp-service-server-sample\local_repository\com\lguplus\onem2m\server-sdk\1.1\server-sdk-1.1.jar!\com\lguplus\onem2m\sdk\server\constant\HttpStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */