from datetime import date
from pydantic import BaseModel
from decimal import Decimal
import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException

# Memuat model yang sudah disimpan
model = joblib.load('CreditCardFraudModel.pkl')

# Membuat aplikasi FastAPI
app = FastAPI()

# Mendefinisikan schema untuk menerima data transaksi
class Transaksi(BaseModel):
    amount: Decimal
    typeOfCard: int  # Menggunakan int untuk type kartu
    entryMode: int   # Menggunakan int untuk entry mode
    transactionType: int  # Menggunakan int untuk tipe transaksi
    countryOfTransaction: int  # Menggunakan int untuk negara transaksi
    gender: int  # Menggunakan int untuk gender
    bank: int  # Menggunakan int untuk bank
    dayOfWeek: int  # Menggunakan int untuk hari dalam minggu
    date: date  # Menggunakan date tanpa waktu

@app.post("/analisis-transaksi/")
async def analisis_transaksi(data: Transaksi):
    try:
        # Mengambil data dari request dan mengonversinya menjadi dictionary
        input_data = data.dict()

        # Buat DataFrame dan sesuaikan urutan kolom sesuai dengan yang dibutuhkan model
        input_data = pd.DataFrame([input_data], columns=["amount", "typeOfCard", "entryMode", "transactionType", 
                                                         "countryOfTransaction", "gender", "bank", "dayOfWeek", "date"])

        # Pastikan data tanggal dalam format yang benar tanpa waktu
        input_data["date"] = pd.to_datetime(input_data["date"]).dt.date

        # Prediksi menggunakan model yang sudah diload
        prediction = model.predict(input_data)

        # Mengembalikan hasil prediksi
        return {"result": "Fraud" if prediction[0] == 1 else "Non-Fraud"}
    
    except HTTPException as http_err:
        raise http_err
    except Exception as e:
        # Menangani kesalahan lain dan memberikan detail pesan kesalahan
        raise HTTPException(status_code=500, detail=f"Error: {str(e)}")
