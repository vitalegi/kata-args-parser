package it.vitalegi.kata.parser;

public interface Parser<E> {

	E getValue(String arg);
}
