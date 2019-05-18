package org.example.arys.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.example.arys.data.Advertisement
import java.util.*

/**
 * This object is for managerial purposes, not used in production.
 */
object DatabaseBuilder {

    private const val WORDS = "words"
    private var firebaseData = FirebaseDatabase.getInstance().reference

    /**
     * Adds [wordItems] to [databaseReference] of the remote database
     *
     * @param databaseReference Parent database reference of the [wordItems]
     * @param wordItems The set to add to the [databaseReference]
     */
    fun addWordItems(databaseReference: DatabaseReference, wordItem: Advertisement) {
        val key = databaseReference.push().key
        wordItem.FirmaID = key
        wordItem.FirmaAdi = 2131165330
        wordItem.FirmaLokasyon = 0
        wordItem.KampanyaIcerik = Login.getUserId()
        wordItem.KampanyaSuresi = Date()
        databaseReference.child(key!!).setValue(wordItem)
    }

    fun updateWordItem(wordItem: WordListItemModel, updateProperty: String) {
        firebaseData.child(WORDS).child(wordItem.uuid!!).child(updateProperty).setValue(wordItem.word_progress)
    }

    fun updateWordItem(wordItem: WordListItemModel) {
        firebaseData.child(WORDS).child(wordItem.uuid!!).setValue(wordItem)
    }

    fun removeWordItem(wordItem: WordListItemModel) {
        firebaseData.child(WORDS).child(wordItem.uuid!!).removeValue()
        WordListItemController.words.remove(wordItem)
    }

    fun resetProgressWordItem(wordItem: WordListItemModel) {
        firebaseData.child(WORDS).child(wordItem.uuid!!).child("word_progress").setValue(0)
        WordListItemController.words.find { w -> w.uuid == wordItem.uuid }!!.word_progress = 0
    }

    fun findWord(word: String): Boolean {
        return WordListItemController.words.find { w -> w.word == word } != null
    }
}