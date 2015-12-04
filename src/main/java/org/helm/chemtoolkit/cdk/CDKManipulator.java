/*******************************************************************************
 * Copyright C 2015, The Pistoia Alliance
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package org.helm.chemtoolkit.cdk;
/**
 * @author chistyakov
 *
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.AttachmentList;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.MoleculeInfo;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.RGroupQueryWriter;
import org.openscience.cdk.io.SMILESWriter;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.ProteinBuilderTool;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

public class CDKManipulator extends AbstractChemistryManipulator {
  // private static final String SMILES_EXTENSION_SEPARATOR_REGEX = "\\|";

  /**
   * 
   * @param smiles extended SMILES (Chemaxon)
   * @return normalized SMILES
   */
  private String normalize(String smiles) {
    String result = null;
    String[] components = smiles.split(SMILES_EXTENSION_SEPARATOR_REGEX);
    result = components[0];

    return result;
  }

  private String normalize(String extendedSmiles, LinkedHashMap<Integer, String> groups) {
    String smiles = null;
    String result = "";
    smiles = normalize(extendedSmiles);
    Iterator<Integer> iterator = groups.keySet().iterator();
    for (char item : smiles.toCharArray()) {
      if (item == '*') {
        result += groups.get(iterator.next());

      } else
        result += item;

    }

    return result;

  }

  /*
   * (non-Javadoc)
   * 
   * 
   * @see org.helm.chemtoolkit.ChemistryManipulator#validateSMILES(java.lang. String)
   */
  @Override
  public boolean validateSMILES(String smiles) {
    smiles = normalize(smiles);
    SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
    try {
      @SuppressWarnings("unused")
      // nothing TODO with molecule, only used for validation
      IAtomContainer molecule = smilesParser.parseSmiles(smiles);

    } catch (InvalidSmilesException e) {

      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * 
   * @see org.helm.chemtoolkit.ChemistryManipulator#getMoleculeInfo(java.lang. String)
   */
  @Override
  public MoleculeInfo getMoleculeInfo(AbstractMolecule aMolecule) throws CTKException {
    IAtomContainer molecule = ((CDKMolecule) aMolecule).molecule;
    MoleculeInfo moleculeInfo = new MoleculeInfo();
// SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
// try {

    // IAtomContainer molecule = smilesParser.parseSmiles(smiles);
    moleculeInfo.setMolecularWeight(AtomContainerManipulator.getNaturalExactMass(molecule));
    moleculeInfo.setMolecularFormula(MolecularFormulaManipulator.getString(MolecularFormulaManipulator.getMolecularFormula(molecule)));

    moleculeInfo.setExactMass(MolecularFormulaManipulator.getMajorIsotopeMass(MolecularFormulaManipulator.getMolecularFormula(molecule)));

    // } catch (InvalidSmilesException e) {
    // throw new CTKSmilesException("smiles is invalid", e);

// }
    return moleculeInfo;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.ChemistryManipulator#convertSMILES2MolFile(java.lang .String)
   */
  @Override
  public String convertSMILES2MolFile(String smiles) throws CTKException {
    String result = null;
    smiles = normalize(smiles);
    try (StringWriter stringWriter = new StringWriter();
        MDLV2000Writer writer = new MDLV2000Writer(stringWriter);) {
      try {
        SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
        IAtomContainer molecule = smilesParser.parseSmiles(smiles);
        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule(molecule);
        sdg.generateCoordinates();
        molecule = sdg.getMolecule();
        writer.writeMolecule(molecule);
        result = stringWriter.toString();
      } catch (InvalidSmilesException e) {
        throw new CTKSmilesException("invalid smiles", e);
      } catch (CDKException e) {
        throw new CTKException("unable to generate coordinates", e);
      } catch (Exception e) {
        throw new CTKException("unable to write molecule", e);
      }
    } catch (IOException e) {
      throw new CTKException("unable to invoke the MDL writer", e);
    }
    return result;

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.ChemistryManipulator#convertMolFile2SMILES(java.lang .String)
   */
  @Override
  public String convertMolFile2SMILES(String molfile) throws CTKException {
    String result = null;

    try (StringWriter stringWriter = new StringWriter();
        SMILESWriter writer = new SMILESWriter(stringWriter);
        StringReader stringReader = new StringReader(molfile);
        MDLV2000Reader reader = new MDLV2000Reader(stringReader)) {
      IAtomContainer molecule =
          reader.read(SilentChemObjectBuilder.getInstance().newInstance(IAtomContainer.class));
      writer.write(molecule);
      result = stringWriter.toString();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      throw new CTKException("illegal argument", e);
    } catch (CDKException e) {
      System.out.println(e.getMessage());
      throw new CTKException("Unable to get a molecule from molfile", e);
    } catch (IOException e) {
      throw new CTKException("unable to invoke the MDL writers/readers", e);

    }

    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.ChemistryManipulator#convert(java.lang.String,
   * org.helm.chemtoolkit.ChemistryManipulator.InputType)
   */
  @Override
  public String convert(String data, InputType type) throws CTKException {
    String result = null;

    switch (type) {
    case SMILES:
      result = convertSMILES2MolFile(data);
      break;
    case MOLFILE:
      result = convertMolFile2SMILES(data);
      break;
    case SEQUENCE:
      result = convertMolFile2SMILES(molecule2Smiles(getPolymer(data)));
      break;
    default:
      break;
    }

    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.ChemistryManipulator#canonicalize(java.lang.String)
   */
  @Override
  public String canonicalize(String smiles) throws CTKException, CTKSmilesException {
    smiles = normalize(smiles);
    SmilesParser parser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
    IAtomContainer molecule;
    String result = null;
    try {
      molecule = parser.parseSmiles(smiles);
      SmilesGenerator generator = SmilesGenerator.unique();
      result = generator.create(molecule);
    } catch (InvalidSmilesException e) {
      throw new CTKSmilesException("invalid SMILES", e);
    } catch (CDKException e) {
      throw new CTKException("unable to canonicalize SMILES", e);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.ChemistryManipulator#renderMol(java.lang.String, int, int, int)
   */
  @Override
  public byte[] renderMol(String molFile, OutputType outputType, int width, int height, int rgb) throws CTKException {
    byte[] result;

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {

      Rectangle drawArea = new Rectangle(width, height);

      try (StringReader stringReader = new StringReader(molFile);
          MDLV2000Reader reader = new MDLV2000Reader(stringReader)) {
        IAtomContainer mol =
            reader.read(SilentChemObjectBuilder.getInstance().newInstance(IAtomContainer.class));

        List<IGenerator<IAtomContainer>> generators = new ArrayList<>();

        generators.add(new BasicSceneGenerator());
        generators.add(new BasicBondGenerator());
        generators.add(new BasicAtomGenerator());

        AtomContainerRenderer renderer = new AtomContainerRenderer(generators, new AWTFontManager());

        renderer.setup(mol, drawArea);

        int scaledw = width / 2;
        int scaledh = (scaledw * 3) / 4;
        BufferedImage scaled = new BufferedImage(scaledw, scaledh, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = scaled.createGraphics();
        g.setBackground(new Color(rgb));
        g.drawImage(scaled, scaledw, scaledh, null);

        renderer.paint(mol, new AWTDrawVisitor(g), new Rectangle2D.Double(0, 0, scaledw, scaledh), true);

        ImageIO.write(scaled, outputType.toString(), ios);
      } catch (IOException e) {
        throw new CTKException("unable to invoke the reader", e);
      } catch (CDKException e) {
        throw new CTKException("invalid molfile", e);
      }

      result = baos.toByteArray();
    } catch (IOException es) {
      throw new CTKException("unable to invoke outputstream");
    }
    return result;

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.ChemistryManipulator#renderSequence(java.lang. String,
   * org.helm.chemtoolkit.ChemistryManipulator.OutputType, int, int, int)
   */
  @Override
  public byte[] renderSequence(String sequence, OutputType outputType, int width, int height, int rgb)
      throws CTKException {
    String molFile;
    try {
      IAtomContainer molecule = getPolymer(sequence);
      molFile = convertSMILES2MolFile(molecule2Smiles(molecule));
      System.out.println(molFile);
    } catch (CTKException e) {
      throw e;
    }

    return renderMol(molFile, outputType, width, height, rgb);

  }

  /**
   * @param molecule
   * @return
   */
  private String molecule2Smiles(IAtomContainer molecule) {
    String result = null;
    try (StringWriter stringWriter = new StringWriter(); SMILESWriter writer = new SMILESWriter(stringWriter)) {
      writer.write(molecule);
      result = stringWriter.toString();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (CDKException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return result;
  }

  private IAtomContainer getPolymer(String sequence) throws CTKException {
    IAtomContainer polymer;
    try {
      polymer = ProteinBuilderTool.createProtein(sequence, SilentChemObjectBuilder.getInstance());
      CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(polymer.getBuilder());
      for (IAtom atom : polymer.atoms()) {
        IAtomType type = matcher.findMatchingAtomType(polymer, atom);
        AtomTypeManipulator.configure(atom, type);
      }
      CDKHydrogenAdder hydrogenAdder = CDKHydrogenAdder.getInstance(polymer.getBuilder());
      hydrogenAdder.addImplicitHydrogens(polymer);

    } catch (CDKException e) {
      throw new CTKException(e.getMessage(), e);
    }

    return polymer;

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractChemistryManipulator#merge(org.helm. chemtoolkit.IMoleculeBase,
   * org.helm.chemtoolkit.IAtomBase, org.helm.chemtoolkit.IMoleculeBase, org.helm.chemtoolkit.IAtomBase)
   */
  @Override
  public AbstractMolecule merge(AbstractMolecule first, IAtomBase firstRgroup, AbstractMolecule second,
      IAtomBase secondRgroup) {
    // TODO Auto-generated method stub
    return null;
  }

  public String createRGroupMolFile(String extendedSmiles) throws CTKException {
    String result = null;
    LinkedHashMap<Integer, String> groups = getRGroupsFromExtendedSmiles(extendedSmiles);

    extendedSmiles = normalize(extendedSmiles, groups);
    try (StringWriter stringWriter = new StringWriter();
        RGroupQueryWriter writer = new RGroupQueryWriter(stringWriter);
        MDLV2000Writer writer2 = new MDLV2000Writer(stringWriter)) {
      SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
      IAtomContainer molecule = smilesParser.parseSmiles(extendedSmiles);

      StructureDiagramGenerator sdg = new StructureDiagramGenerator();
      sdg.setMolecule(molecule);
      sdg.generateCoordinates();
      molecule = sdg.getMolecule();

      for (IAtom atom : molecule.atoms()) {
        if (atom instanceof IPseudoAtom)
          atom.setSymbol("R");
      }
      writer2.write(molecule);

      result = stringWriter.toString();

    } catch (InvalidSmilesException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (CDKException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      throw new CTKException(e.getMessage(), e);
    }

    return result;

  }

  /**
   * @param pseudo
   * @param molecule
   * @param apoBonds
   */
  private void chooseRootAttachmentBonds(IAtom atom, IAtomContainer molecule,
      Map<IAtom, Map<Integer, IBond>> rootAttachmentPoints) {
    int apoIdx = 1;
    Map<Integer, IBond> apoBonds = new HashMap<Integer, IBond>();
    Iterator<IBond> bonds = molecule.bonds().iterator();
    // Pick up to two apo bonds randomly
    while (bonds.hasNext() && apoIdx <= 2) {
      IBond bond = bonds.next();
      if (bond.contains(atom)) {
        apoBonds.put((apoIdx), bond);
        apoIdx++;
      }
    }
    rootAttachmentPoints.put(atom, apoBonds);

  }

  private String convertExtendedSmiles(String extendedSmiles, List<String> groups) {

    char[] parent = extendedSmiles.toCharArray();
    String target = "";
    int index = 1;
    for (char item : parent) {
      if (item == '*') {
        target = target + groups.get(index);
        index++;
      } else
        target = target + item;

    }

    return target;

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractChemistryManipulator#getMolecule(java.lang. String,
   * org.helm.chemtoolkit.AttachmentList)
   */
  @Override
  public AbstractMolecule getMolecule(String smiles, AttachmentList attachments) {
    IAtomContainer molecule = null;

    SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
    try {

      molecule = smilesParser.parseSmiles(smiles);

    } catch (CDKException e) {
    }
    return new CDKMolecule(molecule);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAtomBase removeRgroup(AbstractMolecule molecule, IAtomBase rgroup) {
    // TODO Auto-generated method stub
    return null;
  }

}
