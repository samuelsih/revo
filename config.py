import os
import firebase_admin
from firebase_admin import credentials
from dotenv import load_dotenv

env = os.getenv(key="ENVIRONMENT", default="DEV")
_is_production = True

if env == "DEV":
    _is_production = False
    load_dotenv()


class Config:
    def __init__(self):
        self.is_production = _is_production
        self.fb_app = self.__firebase_app()

    def __firebase_app(self) -> firebase_admin.App:
        cred = credentials.Certificate("sak.json")
        firebase_app = firebase_admin.initialize_app(cred)
        return firebase_app
