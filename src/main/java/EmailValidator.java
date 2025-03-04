public class EmailValidator {
    public static final String PRINTABLE_CHAR = "!#$%&'*+-/=?^_`{|}~";
    public static final char DOT = '.';
    public static final char HYPHEN = '-';
    public static final char AT = '@';

    enum State {
        LOCAL_START, LOCAL_PART, ONE_DOT, DOMAIN_START, DOMAIN_LABEL, DOMAIN_DOT, DOMAIN_HYPHEN, INVALID
    }


    public static boolean isEmailValid(String address){
        State state = State.LOCAL_START;
        char letter = '\u0000';
        int count = 0;
        int totalDomainCount = 0;
        int max = address.length();

        //System.out.println(address);
        for (int len = 0; len < max; len++){
            letter = address.charAt(len);

            if (PRINTABLE_CHAR.indexOf(letter) != -1){
                if (letter == HYPHEN){
                    if (state == State.DOMAIN_LABEL || state == State.DOMAIN_HYPHEN){
                        state = State.DOMAIN_HYPHEN;
                        count +=1;
                        totalDomainCount +=1;
                    }
                    else if (state == State.DOMAIN_START){
                        state = State.INVALID;
                    }
                }
                else{
                    if (state == State.LOCAL_START || state == State.LOCAL_PART ||state == State.ONE_DOT){
                        state = State.LOCAL_PART;
                        count +=1;
                    }
                    else{
                        state = State.INVALID;
                    }
                }

            }

            else if (Character.isLetter(letter) || Character.isDigit(letter)){
                if (state == State.LOCAL_START || state == State.LOCAL_PART || state == State.ONE_DOT){
                    state = State.LOCAL_PART;
                    count +=1;
                }
                else if (state == State.DOMAIN_START || state == State.DOMAIN_LABEL || state == State.DOMAIN_DOT || state == State.DOMAIN_HYPHEN){
                    state = State.DOMAIN_LABEL;
                    count +=1;
                    totalDomainCount +=1;
                }
                else {
                    state = State.INVALID;
                }
            }

            else if (letter == DOT){
                if(state == State.LOCAL_START){
                    state = State.INVALID;
                }
                else if(state == State.LOCAL_PART){
                    state = State.ONE_DOT;
                    count +=1;
                }
                else if (state == State.ONE_DOT){
                    state = State.INVALID;
                }
                else if (state == State.DOMAIN_START){
                    state = State.INVALID;
                }
                else if (state == State.DOMAIN_LABEL){
                    state = State.DOMAIN_DOT;
                    count = 0;
                    totalDomainCount +=1;
                }
                else {
                    state = State.INVALID;
                }
            }

            else if (letter == AT){
                if (state == State.LOCAL_PART){
                    count = 0;
                    state = State.DOMAIN_START;
                }
                else{
                    state = State.INVALID;
                }
            }

            else{ //everything else
                state = State.INVALID;
            }

            if (count > 63 || totalDomainCount > 253){
                state = State.INVALID;
            }
        }
        //System.out.println(state != State.INVALID);
        return state != State.INVALID;
    }
}
