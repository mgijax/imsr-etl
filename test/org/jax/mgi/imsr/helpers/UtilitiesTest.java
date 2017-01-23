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
	// newerFile() Tests
	//
	
	@Test
	public void bothFilesNullReturnsNull() {
		File olderFile = null;
		File newFile = null;
		
		File actualResult = Utilities.newerFile(newFile, olderFile);
		
		assertThat(actualResult, is(equalTo(null)));
	}

	@Test
	public void oneFileNullReturnsTheOtherFile() {
		File olderFile = Mockito.mock(File.class);
		File newFile = null;
		
		File actualResult = Utilities.newerFile(newFile, olderFile);
		
		assertThat(actualResult, is(equalTo(olderFile)));
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
		
		File actualResult = Utilities.newerFile(newFile, oldFile);
		
		assertThat(actualResult, is(equalTo(newFile)));
	}

	
	//
	// pluralSuffix() Tests
	//
	
	@Test
	public void oneCountReturnsSingular() {
		Integer exactlyOneCount = 1;
		String singularString = "test";
		String pluralString = "tests";
		
		String actualResult = Utilities.pluralSuffix(exactlyOneCount, singularString, pluralString);
		
		assertThat(actualResult, is(equalTo(singularString)));
	}
	
	
	@Test
	public void greaterThanOneCountReturnsPlural() {
		Integer greaterThanOneCount = 5;
		String singularString = "test";
		String pluralString = "tests";
		
		String actualResult = Utilities.pluralSuffix(greaterThanOneCount, singularString, pluralString);
		
		assertThat(actualResult, is(equalTo(pluralString)));
	}
	
	@Test
	public void lessThanOneCountReturnsPluralString() {
		Integer lessThanOneCount = 0;
		String singularString = "test";
		String pluralString = "tests";
		
		String actualResult = Utilities.pluralSuffix(lessThanOneCount, singularString, pluralString);
		
		assertThat(actualResult, is(equalTo(pluralString)));
	}
	
	@Test(expected=NullPointerException.class)
	public void throwsExceptionWhenCountIsNull() {
		Integer nullCount = null;
		String singularString = "test";
		String pluralString = "tests";
		
		Utilities.pluralSuffix(nullCount, singularString, pluralString);
	}

	
	//
	// stripDoubleQuotes() Tests
	//
	
	@Test
	public void noQuotesToRemoveFromString() {
		String noDoubleQuotes = "This is a \t valid STRING of characters!";
		
		String actualResult = Utilities.stripDoubleQuotes(noDoubleQuotes);
		
		assertThat(actualResult, is(equalTo(noDoubleQuotes)));
	}
	
	@Test
	public void quotesToBeRemovedFromString() {
		String someDoubleQuotes = "A string - \" \" - with double quotes in the middle.";
		String noDoubleQuotes = "A string -   - with double quotes in the middle.";
		
		String actualResult = Utilities.stripDoubleQuotes(someDoubleQuotes);
		
		assertThat(actualResult, is(equalTo(noDoubleQuotes)));
	}
	
	@Test
	public void emptyStringReturnsEmptyString_DoubleQuoteTest() {
		String emptyString = "";
		
		String actualResult = Utilities.stripDoubleQuotes(emptyString);
		
		assertThat(actualResult, is(equalTo(emptyString)));
	}
	
	@Test(expected=NullPointerException.class)
	public void nullStringReturnsNullString_DoubleQuoteTest() {
		String nullString = null;
		
		Utilities.stripDoubleQuotes(nullString);
	}
	
	//
	// stripNonAsciiChars() Tests
	//
	
	@Test
	public void noCharsRemovedFromValidString() {
		String allValidChars = "This is a \t valid STRING of characters!";
		
		String actualResult = Utilities.stripNonAsciiChars(allValidChars);
		
		assertThat(actualResult, is(equalTo(allValidChars)));
	}
	
	@Test
	public void nonValidCharsRemovedFromString() {
		String someNonValidChars = "Here are valid chars - ¡öÖÊ¿© - with non-valid in the middle.";
		String onlyValidChars = "Here are valid chars -  - with non-valid in the middle.";
		
		String actualResult = Utilities.stripNonAsciiChars(someNonValidChars);
		
		assertThat(actualResult, is(equalTo(onlyValidChars)));
	}
	
	@Test
	public void emptyStringReturnsEmptyString() {
		String emptyString = "";
		
		String actualResult = Utilities.stripNonAsciiChars(emptyString);
		
		assertThat(actualResult, is(equalTo(emptyString)));
	}
	
	@Test(expected=NullPointerException.class)
	public void nullStringReturnsNullString() {
		String nullString = null;
		
		Utilities.stripNonAsciiChars(nullString);
	}
	
}
