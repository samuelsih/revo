from fastapi import FastAPI
from fastapi.responses import JSONResponse
from fastapi.middleware.cors import CORSMiddleware

from schema import SignupSchema
from customizer import http_customize_handler, validation_body_exception_handler
from config import Config
from handler import SignupHandler

config = Config()
signup_handler = SignupHandler(config.fb_app)
app = FastAPI(
    debug=config.is_production, docs_url=None if config.is_production else "/docs"
)

app.add_middleware(
    CORSMiddleware,
    allow_origin=["*"],
    allow_credentials=True,
    allow_methods=["POST", "OPTIONS"],
)

validation_body_exception_handler(app)
http_customize_handler(app)


@app.post("/signup")
async def signup(req: SignupSchema):
    (code, result) = signup_handler.create_user(req.name, req.email, req.password)
    return JSONResponse(status_code=code, content=result)
