from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
from decimal import Decimal
import pandas as pd
import joblib

# Memuat model yang sudah dilatih
model = joblib.load('Model_Prediksi.pkl')

# Menyimpan urutan kolom fitur yang digunakan saat pelatihan
trained_columns = ['dayOfWeek', 'typeOfCard', 'entryMode', 'amount',
                   'typeOfTransaction', 'countryOfTransaction', 'gender', 'bank']

# Membuat aplikasi FastAPI
app = FastAPI()

# Model untuk User
class User(BaseModel):
    userId: int = Field(alias="userId")
    username: str = Field(alias="username")
    email: str = Field(alias="email")

# Model untuk data Transaksi
class Transaksi(BaseModel):
    user: User = Field(alias="user")
    amount: Decimal = Field(alias="amount")
    typeOfCard: int = Field(alias="typeOfCard")
    entryMode: int = Field(alias="entryMode")
    typeOfTransaction: int = Field(alias="typeOfTransaction")
    countryOfTransaction: int = Field(alias="countryOfTransaction")
    gender: int = Field(alias="gender")
    bank: int = Field(alias="bank")
    dayOfWeek: int = Field(alias="dayOfWeek")
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
            "dayOfWeek": input_data["dayOfWeek"],
            "typeOfCard": input_data["typeOfCard"],
            "entryMode": input_data["entryMode"],
            "amount": float(input_data["amount"]),
            "typeOfTransaction": input_data["typeOfTransaction"],
            "countryOfTransaction": input_data["countryOfTransaction"],
            "gender": input_data["gender"],
            "bank": input_data["bank"],
        }

        # Membuat DataFrame dari fitur transaksi
        input_df = pd.DataFrame([features])

        # Menyesuaikan urutan kolom agar sesuai dengan urutan kolom yang digunakan saat pelatihan
        input_df = input_df[trained_columns]

        # Melakukan prediksi
        prediction = model.predict(input_df)[0]

        # Mapping hasil prediksi
        result = "Fraud" if prediction == 1 else "Not Fraud"

        return {
            "user": input_data["user"],
            "result": result
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error: {str(e)}")
