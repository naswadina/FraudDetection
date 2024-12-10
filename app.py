from datetime import date
from pydantic import BaseModel
from decimal import Decimal
import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException
import numpy as np

# Memuat model yang sudah dilatih
model = joblib.load('Model_RF.pkl')

# Membuat aplikasi FastAPI
app = FastAPI()

# Mendefinisikan schema untuk data transaksi
class Transaksi(BaseModel):
    Amount: Decimal
    Type_of_Card: int
    Entry_Mode: int
    Type_of_Transaction: int
    Country_of_Transaction: int
    Gender: int
    Bank: int
    Day_of_Week: int

@app.post("/analisis-transaksi/")
async def analisis_transaksi(data: Transaksi):
    try:
        # Mengonversi data transaksi menjadi dictionary
        input_data = data.dict()

        # Konversi Amount ke float untuk kompatibilitas dengan model
        input_data['Amount'] = float(input_data['Amount'])

        # Membuat DataFrame dari input data
        input_df = pd.DataFrame([input_data])

        # Prediksi menggunakan model
        prediction = model.predict(input_df)

        # Menentukan hasil prediksi
        hasil = "Fraud" if prediction[0] == 1 else "Non-Fraud"

        return {"result": hasil}

    except HTTPException as http_err:
        raise http_err
    except Exception as e:
        # Menangani kesalahan lain dan memberikan detail pesan kesalahan
        raise HTTPException(status_code=500, detail=f"Terjadi kesalahan: {str(e)}")
