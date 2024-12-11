from datetime import date
from pydantic import BaseModel, Field
from decimal import Decimal
import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException

# Memuat model yang sudah dilatih
model = joblib.load('Model_RF.pkl')

# Membuat aplikasi FastAPI
app = FastAPI()

# Model untuk User
class User(BaseModel):
    userId: int = Field(alias="userId")
    username: str = Field(alias="username")
    email: str = Field(alias="email")

# Model untuk data Transaksi
class Transaksi(BaseModel):
    transactionId: int = Field(default=None, alias="transactionId")
    user: User = Field(alias="user")
    amount: Decimal = Field(alias="amount")
    type_of_card: int = Field(alias="typeOfCard")
    entry_mode: int = Field(alias="entryMode")
    type_of_transaction: int = Field(alias="typeOfTransaction")
    country_of_transaction: int = Field(alias="countryOfTransaction")
    gender: int = Field(alias="gender")
    bank: int = Field(alias="bank")
    day_of_week: int = Field(alias="dayOfWeek")
    fraud: bool = Field(default=False, alias="fraud")

    class Config:
        # Menentukan untuk menerima JSON dengan camelCase
        populate_by_name = True

@app.post("/analisis-transaksi/")
async def analisis_transaksi(data: Transaksi):
    try:
        # Konversi data ke dictionary (menggunakan nama asli field)
        input_data = data.dict(by_alias=False)

        # Ekstraksi hanya data transaksi yang diperlukan untuk prediksi
        features = {
            "amount": float(input_data["amount"]),
            "type_of_card": input_data["type_of_card"],
            "entry_mode": input_data["entry_mode"],
            "type_of_transaction": input_data["type_of_transaction"],
            "country_of_transaction": input_data["country_of_transaction"],
            "gender": input_data["gender"],
            "bank": input_data["bank"],
            "day_of_week": input_data["day_of_week"],
        }

        # Membuat DataFrame dari fitur transaksi
        input_df = pd.DataFrame([features])

        # Melakukan prediksi
        prediction = model.predict(input_df)[0]

        # Mapping hasil prediksi
        result = "Fraud" if prediction == 1 else "Not Fraud"

        return {
            "transactionId": input_data["transactionId"],
            "user": input_data["user"],
            "result": result
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error: {str(e)}")
