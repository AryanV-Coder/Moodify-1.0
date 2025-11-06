from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import google.generativeai as genai
import base64
import os
from dotenv import load_dotenv

load_dotenv()

app = FastAPI()

# Allow CORS for frontend apps
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

genai.configure(api_key=os.getenv("GEMINI_API_KEY"))
model = genai.GenerativeModel("gemini-2.5-flash")

@app.get("/start-server")
def start_server():
    print("✅ Server Started Successfully !!")
    return {"status":"success"}

@app.post("/mood-analysis")
async def mood_analysis(image : UploadFile = File(None)):
    if image is not None:
        image_data = await image.read()
        encoded_image = base64.b64encode(image_data).decode('utf-8')
        prompt = [  
            {
                "role": "user",
                "parts": [
                    {
                        "text": """You are a mood detection AI assistant. Your task is to analyze the facial expression and body language of the person in the provided image and determine their emotional state.

CLASSIFICATION RULES:
You must classify the mood into EXACTLY ONE of these four categories:

1. HAPPY - The person displays positive emotions such as:
   - Smiling or laughing
   - Bright, open facial expression
   - Relaxed and joyful demeanor
   - Cheerful body language

2. SAD - The person displays negative or melancholic emotions such as:
   - Frowning or downturned mouth
   - Tears or watery eyes
   - Slumped posture or downcast gaze
   - Somber or dejected expression

3. LOVE - The person displays romantic or affectionate emotions such as:
   - Soft, dreamy facial expression
   - Gentle smile with warm eyes
   - Tender or romantic gestures
   - Heart-shaped hand gestures or loving poses

4. ENERGETIC - The person displays high energy or excitement such as:
   - Wide eyes and animated expression
   - Dynamic or active posture
   - Enthusiastic gestures or movements
   - Vibrant and lively demeanor

OUTPUT REQUIREMENTS:
- Output ONLY the mood category name in CAPITAL LETTERS
- Do NOT include any explanation, commentary, or additional text
- Do NOT use punctuation or formatting
- Valid outputs are: HAPPY, SAD, LOVE, or ENERGETIC
- Choose the MOST dominant mood if multiple emotions are present

Example valid outputs:
HAPPY
SAD
LOVE
ENERGETIC"""
                    }
                ]
            },
            {
                "role": "user",
                "parts": [
                    {
                        "mime_type": "image/png",
                        "data": encoded_image
                    }
                ]
            }
        ]
        
        response = model.generate_content(prompt)
        print(f"✅ Response : {response.text.upper()}")
        return {
            "status" : "success",
            "response" : response.text.upper()
        }
    else :
        print("🛑 NOTHING RECIEVED")
        raise HTTPException(status_code = 400, detail = "Nothing Recieved")