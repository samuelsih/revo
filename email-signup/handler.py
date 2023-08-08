import firebase_admin
from firebase_admin import auth
from firebase_admin._auth_utils import EmailAlreadyExistsError
from typing import Tuple


class SignupHandler:
    def __init__(self, app: firebase_admin.App):
        self.__app = app

    def create_user(self, name: str, email: str, password: str) -> Tuple[int, dict]:
        try:
            auth.create_user(
                app=self.__app,
                display_name=name,
                email=email,
                password=password,
                email_verified=True,
            )

            return (201, {"code": 201, "msg": "Created"})

        except EmailAlreadyExistsError:
            return (400, {"code": 400, "msg": "Email already exists"})

        except Exception as e:
            print(e)
            return (500, {"code": 500, "msg": "Can't signup. Try again later"})
