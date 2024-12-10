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

# Definisikan model untuk validasi data
class User(BaseModel):
    userId: int
    username: str
    email: str

# Mendefinisikan schema untuk data transaksi
class Transaksi(BaseModel):
    amount: Decimal = Field(alias="Amount")
    type_of_card: int = Field(alias="Type_of_Card")
    entry_mode: int = Field(alias="Entry_Mode")
    type_of_transaction: int = Field(alias="Type_of_Transaction")
    country_of_transaction: int = Field(alias="Country_of_Transaction")
    gender: int = Field(alias="Gender")
    bank: int = Field(alias="Bank")
    day_of_week: int = Field(alias="Day_of_Week")
    fraud: bool = False

    class Config:
        # Menentukan untuk tetap menerima JSON dengan CamelCase
        populate_by_name = True

@app.post("/analisis-transaksi/")
async def analisis_transaksi(data: Transaksi):
    try:
        # Mengonversi data transaksi menjadi dictionary
        input_data = data.dict(by_alias=False)  # Gunakan nama asli field

        # Konversi `amount` ke float
        input_data["amount"] = float(input_data["amount"])

        # Membuat DataFrame dari input data
        input_df = pd.DataFrame([input_data])

        # Melakukan prediksi
        prediction = model.predict(input_df)[0]

        # Mapping hasil prediksi
        result = "Fraud" if prediction == 1 else "Not Fraud"

        return {"result": result}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error: {str(e)}")
