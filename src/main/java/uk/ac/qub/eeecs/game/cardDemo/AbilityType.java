package uk.ac.qub.eeecs.game.cardDemo;
/**
 * enums for use with the card ability system
 * to string for use with view card system
 *  Created by &Justin johnston <40237507>
 * @version 1.0
 */
public enum AbilityType {
        DEFEND{
                public String toString() {
                        return "Stop opponent from attacking your avatar.";
                }
         }
        ,MANA{
                public String toString() {
                        return "Gain 2 mana when you play this card.";
                }
        }
        ,RUSH{
                public String toString() {
                        return "These can attack on their first move.";
                }
        }
        ,NONE{
                public String toString() {
                        return "No special abilities.";
                }
        }
}