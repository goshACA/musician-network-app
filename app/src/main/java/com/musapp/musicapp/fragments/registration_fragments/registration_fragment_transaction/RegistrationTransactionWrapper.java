package com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction;

public final class RegistrationTransactionWrapper {
    private RegistrationTransactionWrapper(){}
    private static RegisterFragmentTransaction registerFragmentTransaction;

    public static void registerForNextFragment(int id){
        registerFragmentTransaction.getNextFragment(id);
    }

    public static void setRegisterFragmentTransaction(RegisterFragmentTransaction registerFragmentTransaction) {
        RegistrationTransactionWrapper.registerFragmentTransaction = registerFragmentTransaction;
    }
}
