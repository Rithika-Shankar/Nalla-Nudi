package com.example.nallanudi.database

import android.content.Context
import com.example.nallanudi.models.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object DatabasePreload {

    fun preloadData(context: Context, wordDao: WordDao) {
        CoroutineScope(Dispatchers.IO).launch {
            // Check if data already exists
            if (wordDao.searchWords("").first().isNotEmpty()) {
                return@launch
            }

            val words = listOf(
                // Science Terms
                Word(
                    term = "Photosynthesis",
                    kannadaMeaning = "ದ್ಯುತಿಸಂಶ್ಲೇಷಣೆ",
                    explanation = "ಸಸ್ಯಗಳು ಸೂರ್ಯನ ಬೆಳಕನ್ನು ಶಕ್ತಿಯಾಗಿ ಪರಿವರ್ತಿಸುವ ಪ್ರಕ್ರಿಯೆ",
                    exampleSentence = "Plants make their own food through photosynthesis.",
                    subject = "Science"
                ),
                Word(
                    term = "Gravity",
                    kannadaMeaning = "ಗುರುತ್ವಾಕರ್ಷಣೆ",
                    explanation = "ವಸ್ತುಗಳನ್ನು ಭೂಮಿಯ ಕೇಂದ್ರದ ಕಡೆಗೆ ಎಳೆಯುವ ಶಕ್ತಿ",
                    exampleSentence = "Apples fall down because of gravity.",
                    subject = "Science"
                ),
                Word(
                    term = "Ecosystem",
                    kannadaMeaning = "ಪರಿಸರ ವ್ಯವಸ್ಥೆ",
                    explanation = "ಜೀವಿಗಳು ಮತ್ತು ಅವುಗಳ ಪರಿಸರದ ನಡುವಿನ ಸಂಬಂಧ",
                    exampleSentence = "A pond is a small ecosystem.",
                    subject = "Science"
                ),
                Word(
                    term = "Molecule",
                    kannadaMeaning = "ಅಣು",
                    explanation = "ಎರಡು ಅಥವಾ ಹೆಚ್ಚಿನ ಪರಮಾಣುಗಳ ರಾಸಾಯನಿಕ ಬಂಧನದಿಂದಾದ ಕಣ",
                    exampleSentence = "Water is made of H2O molecules.",
                    subject = "Science"
                ),
                Word(
                    term = "Energy",
                    kannadaMeaning = "ಶಕ್ತಿ",
                    explanation = "ಕೆಲಸ ಮಾಡುವ ಸಾಮರ್ಥ್ಯ",
                    exampleSentence = "The sun provides energy for all life on Earth.",
                    subject = "Science"
                ),

                // Math Terms
                Word(
                    term = "Trigonometry",
                    kannadaMeaning = "ತ್ರಿಕೋನಮಿತಿ",
                    explanation = "ತ್ರಿಕೋನಗಳ ಬಾಹುಗಳು ಮತ್ತು ಕೋನಗಳ ನಡುವಿನ ಸಂಬಂಧವನ್ನು ಅಧ್ಯಯನ ಮಾಡುವ ಗಣಿತದ ಶಾಖೆ",
                    exampleSentence = "Trigonometry helps calculate the height of mountains.",
                    subject = "Math"
                ),
                Word(
                    term = "Algebra",
                    kannadaMeaning = "ಬೀಜಗಣಿತ",
                    explanation = "ಸಂಖ್ಯೆಗಳನ್ನು ಅಕ್ಷರಗಳಿಂದ ಪ್ರತಿನಿಧಿಸುವ ಗಣಿತದ ಶಾಖೆ",
                    exampleSentence = "Find the value of x using algebra.",
                    subject = "Math"
                ),
                Word(
                    term = "Geometry",
                    kannadaMeaning = "ರೇಖಾಗಣಿತ",
                    explanation = "ಆಕಾರಗಳು, ಗಾತ್ರಗಳು ಮತ್ತು ಸ್ಥಳಗಳ ಗುಣಲಕ್ಷಣಗಳ ಅಧ್ಯಯನ",
                    exampleSentence = "Geometry helps in drawing buildings.",
                    subject = "Math"
                ),
                Word(
                    term = "Calculus",
                    kannadaMeaning = "ಕಲನಶಾಸ್ತ್ರ",
                    explanation = "ಬದಲಾವಣೆ ಮತ್ತು ಚಲನೆಯನ್ನು ಅಧ್ಯಯನ ಮಾಡುವ ಗಣಿತದ ಶಾಖೆ",
                    exampleSentence = "Calculus is used in physics and engineering.",
                    subject = "Math"
                ),
                Word(
                    term = "Probability",
                    kannadaMeaning = "ಸಂಭವನೀಯತೆ",
                    explanation = "ಒಂದು ಘಟನೆ ಸಂಭವಿಸುವ ಸಾಧ್ಯತೆಯ ಅಳತೆ",
                    exampleSentence = "The probability of getting heads is 1/2.",
                    subject = "Math"
                ),

                // Commerce Terms
                Word(
                    term = "Revenue",
                    kannadaMeaning = "ಆದಾಯ",
                    explanation = "ವ್ಯಾಪಾರದಿಂದ ಗಳಿಸಿದ ಒಟ್ಟು ಹಣ",
                    exampleSentence = "The company's revenue increased by 20%.",
                    subject = "Commerce"
                ),
                Word(
                    term = "Asset",
                    kannadaMeaning = "ಆಸ್ತಿ",
                    explanation = "ಯಾವುದೇ ಮೌಲ್ಯಯುತ ವಸ್ತು ಅಥವಾ ಸಂಪನ್ಮೂಲ",
                    exampleSentence = "The building is a company asset.",
                    subject = "Commerce"
                ),
                Word(
                    term = "Liability",
                    kannadaMeaning = "ಹೊಣೆಗಾರಿಕೆ",
                    explanation = "ಕಂಪನಿಯು ಇತರರಿಗೆ ನೀಡಬೇಕಾದ ಹಣ ಅಥವಾ ಸೇವೆಗಳು",
                    exampleSentence = "Bank loans are liabilities.",
                    subject = "Commerce"
                ),
                Word(
                    term = "Profit",
                    kannadaMeaning = "ಲಾಭ",
                    explanation = "ಆದಾಯ ಮತ್ತು ವೆಚ್ಚದ ನಡುವಿನ ವ್ಯತ್ಯಾಸ",
                    exampleSentence = "Profit = Revenue - Expenses.",
                    subject = "Commerce"
                ),
                Word(
                    term = "Investment",
                    kannadaMeaning = "ಹೂಡಿಕೆ",
                    explanation = "ಭವಿಷ್ಯದ ಲಾಭಕ್ಕಾಗಿ ಹಣವನ್ನು ನಿಯೋಜಿಸುವುದು",
                    exampleSentence = "Investing in education pays long-term dividends.",
                    subject = "Commerce"
                )
            )

            words.forEach { word ->
                wordDao.insertWord(word)
            }
        }
    }
}