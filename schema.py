from pydantic import BaseModel, Field, EmailStr


class SignupSchema(BaseModel):
    name: str = Field(min_length=3, max_length=50)
    email = EmailStr()
    password: str = Field(min_length=8, max_length=30)
