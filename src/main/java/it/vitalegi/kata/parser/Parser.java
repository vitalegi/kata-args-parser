package it.vitalegi.kata.parser;

public abstract class Parser<E> {

	public abstract E getValue(String arg);
}
