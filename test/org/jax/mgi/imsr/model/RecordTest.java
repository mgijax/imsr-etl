package org.jax.mgi.imsr.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

public class RecordTest {

	//
	// createRecord() Tests
	//
	@Test
	public void createRecordWithoutStrainUrlAndOnlyRequiredFields() {
		String lineWithoutUrlAndOnlyRequiredFields = "ID1234\tBJ6/Pax6\tMSR\tLM";
		Record expectedRecord = new Record();
		expectedRecord.setId("ID1234");
		expectedRecord.setName("BJ6/Pax6");
		expectedRecord.setTypes(Sets.newSet("MSR"));
		expectedRecord.setStates(Sets.newSet("LM"));
		
		Record actualResult = Record.createRecord(lineWithoutUrlAndOnlyRequiredFields, false);
		
		assertThat(actualResult, is(equalTo(expectedRecord)));
	}

	@Test
	public void createRecordWithoutStrainUrlAndAllFields() {
		String lineWithoutUrlAndOnlyRequiredFields = "ID1234\tBJ6/Pax6\tMSR\tLM\tMGI:90909\tKras<tm1>\tAllele Name for kras"
				+ "\tTM\t2\tMGI:40404\tkras\tGene Name for kras";
		Record expectedRecord = new Record();
		expectedRecord.setId("ID1234");
		expectedRecord.setName("BJ6/Pax6");
		expectedRecord.setTypes(Sets.newSet("MSR"));
		expectedRecord.setStates(Sets.newSet("LM"));
		expectedRecord.setMgiAlleleAccId("MGI:90909");
		expectedRecord.setAlleleSymbol("Kras<tm1>");
		expectedRecord.setAlleleName("Allele Name for kras");
		expectedRecord.setMutationTypes(Sets.newSet("TM"));
		expectedRecord.setChromosome("2");
		expectedRecord.setMgiGeneAccId("MGI:40404");
		expectedRecord.setGeneSymbol("kras");
		expectedRecord.setGeneName("Gene Name for kras");
		
		Record actualResult = Record.createRecord(lineWithoutUrlAndOnlyRequiredFields, false);
		
		assertThat(actualResult, is(equalTo(expectedRecord)));
	}

	@Test
	public void createRecordWithStrainUrlAndOnlyRequiredFields() {
		String lineWithoutUrlAndOnlyRequiredFields = "ID1234\tBJ6/Pax6\tMSR\tLM\twww.required.url";
		Record expectedRecord = new Record();
		expectedRecord.setId("ID1234");
		expectedRecord.setName("BJ6/Pax6");
		expectedRecord.setTypes(Sets.newSet("MSR"));
		expectedRecord.setStates(Sets.newSet("LM"));
		expectedRecord.setUrl("www.required.url");
		
		Record actualResult = Record.createRecord(lineWithoutUrlAndOnlyRequiredFields, true);
		
		assertThat(actualResult, is(equalTo(expectedRecord)));
	}

	@Test
	public void createRecordWithStrainUrlAndAllFields() {
		String lineWithoutUrlAndOnlyRequiredFields = "ID1234\tBJ6/Pax6\tMSR\tLM\twww.required.url\tMGI:90909\tKras<tm1>\tAllele Name for kras"
				+ "\tTM\t2\tMGI:40404\tkras\tGene Name for kras";
		Record expectedRecord = new Record();
		expectedRecord.setId("ID1234");
		expectedRecord.setName("BJ6/Pax6");
		expectedRecord.setTypes(Sets.newSet("MSR"));
		expectedRecord.setStates(Sets.newSet("LM"));
		expectedRecord.setUrl("www.required.url");
		expectedRecord.setMgiAlleleAccId("MGI:90909");
		expectedRecord.setAlleleSymbol("Kras<tm1>");
		expectedRecord.setAlleleName("Allele Name for kras");
		expectedRecord.setMutationTypes(Sets.newSet("TM"));
		expectedRecord.setChromosome("2");
		expectedRecord.setMgiGeneAccId("MGI:40404");
		expectedRecord.setGeneSymbol("kras");
		expectedRecord.setGeneName("Gene Name for kras");
		
		Record actualResult = Record.createRecord(lineWithoutUrlAndOnlyRequiredFields, true);
		
		assertThat(actualResult, is(equalTo(expectedRecord)));
	}

	@Test
	public void createRecordWithMultipleSetElements() {
		String lineWithoutUrlAndOnlyRequiredFields = "ID1234\tBJ6/Pax6\tIS, MSR\tLM, SP, ES\twww.required.url\tMGI:90909\tKras<tm1>\tAllele Name for kras"
				+ "\tTM\t2\tMGI:40404\tkras\tGene Name for kras";
		Record expectedRecord = new Record();
		expectedRecord.setId("ID1234");
		expectedRecord.setName("BJ6/Pax6");
		expectedRecord.setTypes(Sets.newSet("MSR", "IS"));
		expectedRecord.setStates(Sets.newSet("LM", "ES", "SP"));
		expectedRecord.setUrl("www.required.url");
		expectedRecord.setMgiAlleleAccId("MGI:90909");
		expectedRecord.setAlleleSymbol("Kras<tm1>");
		expectedRecord.setAlleleName("Allele Name for kras");
		expectedRecord.setMutationTypes(Sets.newSet("TM"));
		expectedRecord.setChromosome("2");
		expectedRecord.setMgiGeneAccId("MGI:40404");
		expectedRecord.setGeneSymbol("kras");
		expectedRecord.setGeneName("Gene Name for kras");
		
		Record actualResult = Record.createRecord(lineWithoutUrlAndOnlyRequiredFields, true);
		
		assertThat(actualResult, is(equalTo(expectedRecord)));
	}

}
