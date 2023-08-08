import firebase_admin
from firebase_admin import credentials


class Config:
    def __init__(self):
        self.fb_app = self.__firebase_app()

    def __firebase_app(self) -> firebase_admin.App:
        cred = credentials.Certificate("sak.json")
        firebase_app = firebase_admin.initialize_app(cred)
        return firebase_app
