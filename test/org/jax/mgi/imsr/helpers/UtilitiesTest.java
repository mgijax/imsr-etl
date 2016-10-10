package org.jax.mgi.imsr.helpers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

public class UtilitiesTest {

	//
	// pluralSuffix() Tests
	//
	
	@Test
	public void oneCountReturnsSingular() {
		Integer exactlyOneCount = 1;
		String singularString = "test";
		String pluralString = "tests";
		
		String result = Utilities.pluralSuffix(exactlyOneCount, singularString, pluralString);
		
		assertThat(result, is(equalTo(singularString)));
	}
	
	
	@Test
	public void greaterThanOneCountReturnsPlural() {
		Integer greaterThanOneCount = 5;
		String singularString = "test";
		String pluralString = "tests";
		
		String result = Utilities.pluralSuffix(greaterThanOneCount, singularString, pluralString);
		
		assertThat(result, is(equalTo(pluralString)));
	}
	
	@Test
	public void lessThanOneCountReturnsPluralString() {
		Integer lessThanOneCount = 0;
		String singularString = "test";
		String pluralString = "tests";
		
		String result = Utilities.pluralSuffix(lessThanOneCount, singularString, pluralString);
		
		assertThat(result, is(equalTo(pluralString)));
	}
	
	@Test(expected=NullPointerException.class)
	public void throwsExceptionWhenCountIsNull() {
		Integer nullCount = null;
		String singularString = "test";
		String pluralString = "tests";
		
		Utilities.pluralSuffix(nullCount, singularString, pluralString);
	}

	
	//
	// newerFile() Tests
	//
	
	@Test
	public void bothFilesNullReturnsNull() {
		File olderFile = null;
		File newFile = null;
		
		File result = Utilities.newerFile(newFile, olderFile);
		
		assertThat(result, is(equalTo(null)));
	}

	@Test
	public void oneFileNullReturnsTheOtherFile() {
		File olderFile = Mockito.mock(File.class);
		File newFile = null;
		
		File result = Utilities.newerFile(newFile, olderFile);
		
		assertThat(result, is(equalTo(olderFile)));
	}

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Test
	public void returnsTheNewerFile() throws IOException {
		final File oldFile = tempFolder.newFile("oldFile.tmp");
		final File newFile = tempFolder.newFile("newFile.tmp");
		Integer olderTime = 100;
		Integer newerTime = 10000;
		
		oldFile.setLastModified(olderTime);
		newFile.setLastModified(newerTime);
		
		File result = Utilities.newerFile(newFile, oldFile);
		
		assertThat(result, is(equalTo(newFile)));
	}

	
	//
	// stripNonAsciiChars() Tests
	//
	
	@Test
	public void noCharsRemovedFromValidString() {
		String allValidChars = "This is a \t valid STRING of characters!";
		
		String result = Utilities.stripNonAsciiChars(allValidChars);
		
		assertThat(result, is(equalTo(allValidChars)));
	}
	
	@Test
	public void nonValidCharsRemovedFromString() {
		String someNonValidChars = "Here are valid chars - ¡öÖÊ¿© - with non-valid in the middle.";
		String onlyValidChars = "Here are valid chars -  - with non-valid in the middle.";
		
		String result = Utilities.stripNonAsciiChars(someNonValidChars);
		
		assertThat(result, is(equalTo(onlyValidChars)));
	}
	
	@Test
	public void emptyStringReturnsEmptyString() {
		String emptyString = "";
		
		String result = Utilities.stripNonAsciiChars(emptyString);
		
		assertThat(result, is(equalTo(emptyString)));
	}
	
	@Test(expected=NullPointerException.class)
	public void nullStringReturnsNullString() {
		String nullString = null;
		
		Utilities.stripNonAsciiChars(nullString);
	}
	
}
