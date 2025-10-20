# Data Schema
ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode
- session_id: anonymous cookie (e.g., 6–8 chars)
- request_id: random per task attempt
- step: start/success/fail/validation_error/server_error
- ms: duration of the attempt (server-measured)

