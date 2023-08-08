import functions_framework
from flask import Request
from pydantic import ValidationError

from schema import SignupSchema
from config import Config
from handler import SignupHandler

config = Config()
signup_handler = SignupHandler(config.fb_app)


@functions_framework.http
def signup(r: Request):
    if r.method != "POST":
        return {"code": 405, "msg": "Method not allowed"}, 405

    try:
        user = SignupSchema.parse_raw(r.get_data())
        (code, result) = signup_handler.create_user(
            user.name, user.email, user.password
        )

        return result, code

    except ValidationError as error:
        raw_err = error.errors()
        resp = {"code": 422, "msg": "Unprocessable Entity", "errors": {}}

        for e in raw_err:
            resp["errors"][e["loc"][0]] = e["msg"]

        return resp, 422
