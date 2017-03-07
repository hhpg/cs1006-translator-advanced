import java.util.ArrayList;

class rule {
    /**
     * Array list that stores each translated token
     */
    private ArrayList<word> espWords = new ArrayList<>();
    /**
     * boolean value that indicates whether the noun is masculine or not
     */
    private boolean isMasc = true;

    /**
     * Constructor that sets the Array List translatedTokens field to the given Array List and calls the grammarRules()
     * methodto enforce all grammar rules
     *
     * @param tokens Array List of the tokens to translate
     */
    rule(ArrayList<word> tokens) {
        this.espWords = tokens;
        grammarRules();
    }

    /**
     * Method that checks what the grammar type of the English word "her" is, i.e. it can be a pronoun or a possessive,
     * by checking the type of the word that is in front of it; if the word in front of it is a noun or an adjective then
     * the word type will be possessive rather than a pronoun and the translated name of the word will be "su" as
     * opposed to "ella", so this method enforces this grammar rule; let this rule be called rule 0.
     */
    private void checkTypeOfHer() {
        for (int i = 0; i < espWords.size() - 1; i++) {
            if (espWords.get(i).getName().equals("ella") && (espWords.get(i + 1).getType().equals("noun") ||
                    espWords.get(i + 1).getType().equals("adjective"))) {
                espWords.get(i).setName("su");
                espWords.get(i).setType("possessive");
            }
        }
    }

    /**
     * Method that enforces grammar rule 3, that is; the adjectives should always be followed by the noun
     */
    private void swapNounAndAdj() {

        /* if there isn't an article present in the list then the following for loop is executed in */
        for (int i = 1; i < espWords.size(); i++) { /* Checks for unordered adjective/noun stuff */
            if ((espWords.get(i).getType().equals("adjective") ||  espWords.get(i).getName().equals("y")) && (espWords.get(i - 1).getType().equals("noun"))) {
                word x = espWords.get(i);
                word y = espWords.get(i - 1);
                espWords.set(i - 1, x);
                espWords.set(i, y);
            }
        }
    }

    /**
     * Method that sets the isPlural field of the word objects to true if the nouns are plural
     */
    private void setPlurals() {

        for (int i = 0; i < espWords.size(); i++) {
            if (i > 0 && espWords.get(i).getType().equals("adjective") && espWords.get(i - 1).getType().equals("noun") && espWords.get(i - 1).getIsPlural()) {
                espWords.get(i).setPlural(true);
            } else if (i > 1 && espWords.get(i).getType().equals("adjective") && espWords.get(i - 2).getType().equals("noun") && espWords.get(i - 2).getIsPlural()) {
                espWords.get(i).setPlural(true);
            } else if (i < espWords.size() - 1 && espWords.get(i).getType().equals("article") && isMasc && espWords.get(i + 1).getType().equals("noun") && espWords.get(i + 1).getIsPlural()) {
                espWords.get(i).setPlural(true);
            } else if (i < espWords.size() - 1 && espWords.get(i).getType().equals("article") && !isMasc && espWords.get(i + 1).getType().equals("noun") && espWords.get(i + 1).getIsPlural()) {
                espWords.get(i).setPlural(true);
            } else if (i < espWords.size() - 1 && espWords.get(i).getType().equals("possessive") && espWords.get(i + 1).getIsPlural()) {
                espWords.get(i).setPlural(true);
            }
        }
    }

    /**
     * Method that calls the setPlurals() methods to set the isPlural fields of each word object, then
     * decides what to do based on the value of the isPlural field (i.e. if the isPlural field has a value of true
     * then make the word being inspected plural if it an adjective or article) (rule 4)
     */
    private void ensurePlurality() {

        setPlurals();
        String noun, adj, article, possessive;
        /* Rule 2 and 4 */
        for (int i = 0; i < espWords.size(); i++)
            if (espWords.get(i).getType().equals("adjective") && espWords.get(i).getIsPlural()) {
                adj = espWords.get(i).getName();
                espWords.get(i).makePlural(adj, i);
            } else if (espWords.get(i).getType().equals("article") && isMasc && espWords.get(i).getIsPlural()) {
                article = "los";
                espWords.get(i).setName(article);
            } else if (espWords.get(i).getType().equals("article") && !isMasc && espWords.get(i).getIsPlural()) {
                article = "las";
                espWords.get(i).setName(article);
            } else if (espWords.get(i).getType().equals("noun") && espWords.get(i).getIsPlural()) {
                noun = espWords.get(i).getName();
                espWords.get(i).makePlural(noun, i);
            } else if (espWords.get(i).getType().equals("possessive") && espWords.get(i).getIsPlural()) {
                possessive = espWords.get(i).getName();
                espWords.get(i).makePlural(possessive, i);
            }
    }

    /**
     * Spanish differentiates "are" based on the person, so this method determines what "are" should be replaced with (rule 5)
     */
    private void replaceAre() {
        for (int i = 0; i < espWords.size() - 1; i++) { /* rule 5 */
            if (espWords.get(i).getName().equals("tú") && espWords.get(i + 1).getName().equals("eres")) {
                espWords.get(i + 1).setName("son");
            } else if (espWords.get(i).getName().equals("nosotros") && espWords.get(i + 1).getName().equals("somos")) {
                espWords.get(i + 1).setName("son");
            }
        }
    }

    /**
     * soy/eres/somos/es/son becomes “est**” based on the noun/pronoun when followed by a
     * verb or “with”, hence this method ensures that this rule is enforced (rule 6)
     */
    private void changeFromEst() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("él");
        list.add("eso");
        list.add("ella");

        for (int i = 0; i < espWords.size() - 1; i++) {
            if (espWords.get(i).getName().equals("yo") && espWords.get(i+1).getName().equals("estoy")) {
                espWords.get(i+1).setName("soy");
            } else if (espWords.get(i).getName().equals("tú") && espWords.get(i+1).getName().equals("estás")) {
                espWords.get(i+1).setName("eres");
            } else if (list.contains(espWords.get(i).getName()) && espWords.get(i+1).getName().equals("está")) {
                espWords.get(i+1).setName("es");
            } else if (espWords.get(i).getName().equals("nosotros") && espWords.get(i+1).getName().equals("estamos")) {
                espWords.get(i+1).setName("somos");
            } else if (espWords.get(i).getName().equals("ellos") && espWords.get(i+1).getName().equals("están")) {
                espWords.get(i+1).setName("son");
            }
        }
    }

    /**
     * Spanish usually drops pronouns with es/est*** forms, hence this method enforces this rule (rule 7)
     */
    private void undropPronouns() {

        for (int i = 0; i < espWords.size() - 1; i++) {
            if (espWords.get(i).getName().equals("estoy") && i==0) {
                word x = new word();
                x.setName("yo");
                x.setType("pronoun");
                espWords.add(0, x);

            } else if (espWords.get(i).getName().equals("estamos") && i==0) {
                word x = new word();
                x.setName("nosotros");
                x.setType("pronoun");
                espWords.add(0, x);

            }else if (espWords.get(i).getName().equals("estás") && i==0) {
                word x = new word();
                x.setName("tú");
                x.setType("pronoun");
                espWords.add(0, x);
            } else if (espWords.get(i).getName().equals("están") && i==0) {
                word x = new word();
                x.setName("ellos");
                x.setType("pronoun");
                espWords.add(0, x);
            }
        }
    }

    /**
     * Method that swaps a token with the token that is in front of it (in front of being the position of the token + 1)
     *
     * @param name     the name of token that needs to be swapped
     * @param type     the type of the token that needs to be swapped
     * @param position the position of the token within the translatedTokens array list
     */
    private void swapTokens(String name, String type, int position) {
        espWords.get(position).setName(espWords.get(position - 1).getName());
        espWords.get(position).setType(espWords.get(position - 1).getType());
        espWords.get(position - 1).setName(espWords.get(position - 2).getName());
        espWords.get(position - 1).setType(espWords.get(position - 2).getType());
        espWords.get(position - 2).setName(name);
        espWords.get(position - 2).setType(type);
    }

    /**
     * Method that checks if some pronoun is followed by some verb, and if so, enforces rule 8
     */
    private void pronounFollowedByVerb() {
        for (int i = 0; i < espWords.size(); i++) {
            /* you are watching me */
            if (espWords.get(i).getName().equals("yo") && espWords.get(i - 1).getType().equals("verb")) {
                swapTokens("me", "pronoun", i);
            }
            /* they are watching you */
            else if (espWords.get(i).getName().equals("tú") && espWords.get(i - 1).getType().equals("verb")) {
                swapTokens("te", "pronoun", i);
            }
            /* i am watching him */
            else if (espWords.get(i).getName().equals("él") && espWords.get(i - 1).getType().equals("verb")) {
                swapTokens("lo", "pronoun", i);
            } else if (espWords.get(i).getName().equals("ella") && espWords.get(i - 1).getType().equals("verb")) {
                swapTokens("la", "pronoun", i);
            } else if (espWords.get(i).getName().equals("ellos") && espWords.get(i - 1).getType().equals("verb")) {
                swapTokens("los", "pronoun", i);
            }
        }
    }

    /**
     * Method that checks if the sentence contains some negation, and if so, enforces rule 9
     */
    private void negation() {
        for (int i = 1; i < espWords.size() - 1; i++) {
            if (espWords.get(i).getName().equals("no")) {
                word neg, verb;
                neg = espWords.get(i);
                verb = espWords.get(i + 1);
                espWords.set(i + 1, neg);
                espWords.set(i, verb);
                // if(translatedTokens.get(i+1).getType().equals("punctuation")) {
                //   translatedTokens.remove(i+1);
                // }
            }
        }
    }

    /**
     * In Spanish, some replacements are usaully done, i.e. with me --> con mi --> conmigo, with you --> con tú --> contigo,
     * so this method ensures this rule is followed (rule 10).
     */
    private void replacements() {
        for (int i = 0; i < espWords.size()-1; i++) {
            if (espWords.get(i).getName().equals("contigo")) {
                espWords.get(i).setName("con");
                word x = new word();
                x.setName("tú");
                espWords.add(i+1, x);
                /* removed i originally so now i + 1 == i */
                //if(translatedTokens.get(i).getType().equals("punctuation")) {
                //  translatedTokens.remove(i);
                //}
            } //else if (espWords.get(i).getType().equals("preposition") && espWords.get(i + 1).getName().equals("ti")) {
               // espWords.get(i + 1).setName("tú");
            //}
            if (espWords.get(i).getName().equals("conmigo") && espWords.get(i + 1).getName().equals("yo")) {
                espWords.get(i).setName("con");
                word x = new word();
                x.setName("tú");
                espWords.add(i+1, x);
                //if(translatedTokens.get(i).getType().equals("punctuation")) {
                //  translatedTokens.remove(i);
                // }
            }
        }
    }

    /**
     * Method that calls all of the above grammar rule methods to enforce all
     * rules onto the translatedTokens Array List
     */
    private void grammarRules() {
        //checkTypeOfHer();
        swapNounAndAdj();
        //ensurePlurality();
        replaceAre();
        undropPronouns();
        negation();
        changeFromEst();
        //pronounFollowedByVerb();
        replacements();
    }
}