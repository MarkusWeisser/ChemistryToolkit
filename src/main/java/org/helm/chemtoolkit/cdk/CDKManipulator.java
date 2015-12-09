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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.AttachmentList;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.IBondBase;
import org.helm.chemtoolkit.IStereoElementBase;
import org.helm.chemtoolkit.MoleculeInfo;
import org.openscience.cdk.Bond;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.aromaticity.Aromaticity;
import org.openscience.cdk.aromaticity.ElectronDonation;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.graph.CycleFinder;
import org.openscience.cdk.graph.Cycles;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
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
import org.openscience.cdk.stereo.TetrahedralChirality;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.ProteinBuilderTool;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CDKManipulator extends AbstractChemistryManipulator {
  // private static final String SMILES_EXTENSION_SEPARATOR_REGEX = "\\|";
  private static final Logger LOG = LoggerFactory.getLogger(CDKManipulator.class);

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
      if (item == '*' && iterator.hasNext()) {
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
    IAtomContainer molecule = (IAtomContainer) aMolecule.getMolecule();
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

  private String convertSMILES2MolFile(String smiles) throws CTKException {
    String result = null;

    try (StringWriter stringWriter = new StringWriter();
        MDLV2000Writer writer = new MDLV2000Writer(stringWriter);) {
      try {

        IAtomContainer molecule = getIAtomContainer(smiles);

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

  private String convertMolFile2SMILES(String molfile) throws CTKException {
    String result = null;

    try (StringReader stringReader = new StringReader(molfile);
        MDLV2000Reader reader = new MDLV2000Reader(stringReader)) {

      IAtomContainer molecule =
          reader.read(DefaultChemObjectBuilder.getInstance().newInstance(IAtomContainer.class));

      ElectronDonation model = ElectronDonation.cdk();
      CycleFinder cycles = Cycles.cdkAromaticSet();
      Aromaticity aromaticity = new Aromaticity(model, cycles);

      // apply our configured model to each molecule, the CDK model
      // requires that atom types are perceived

      AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
      aromaticity.apply(molecule);

      CDKMolecule container = new CDKMolecule(molecule);
      result = convertMolecule(container, StType.SMILES);

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
  public String convert(String data, StType type) throws CTKException {
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
    IAtomContainer molecule = getIAtomContainer(smiles);
    SmilesGenerator generator = SmilesGenerator.unique();
    String result = null;
    try {
      result = generator.create(molecule);
    } catch (CDKException e) {
      throw new CTKSmilesException("invalid smiles", e);
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
    IAtomContainer molecule = getPolymer(sequence);
    molFile = convertSMILES2MolFile(molecule2Smiles(molecule));
    System.out.println(molFile);

    return renderMol(molFile, outputType, width, height, rgb);

  }

  /**
   * @param molecule
   * @return
   * @throws CTKException
   * @throws IOException
   * @throws CDKException
   */
  private String molecule2Smiles(IAtomContainer molecule) throws CTKException {
    String result = null;

    SmilesGenerator generator = SmilesGenerator.isomeric();
    try {
      result = generator.create(molecule);
    } catch (CDKException e) {
      throw new CTKException(e.getMessage(), e);
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

// public String createRGroupMolFile(String extendedSmiles) throws CTKException {
// String result = null;
// LinkedHashMap<Integer, String> groups = getRGroupsFromExtendedSmiles(extendedSmiles);
//
// extendedSmiles = normalize(extendedSmiles, groups);
//
// try (StringWriter stringWriter = new StringWriter();
// // RGroupQueryWriter writer = new RGroupQueryWriter(stringWriter);
// MDLV2000Writer writer2 = new MDLV2000Writer(stringWriter)) {
// SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
// IAtomContainer molecule = smilesParser.parseSmiles(extendedSmiles);
//
// StructureDiagramGenerator sdg = new StructureDiagramGenerator();
// sdg.setMolecule(molecule);
// sdg.generateCoordinates();
// molecule = sdg.getMolecule();
//
// for (IAtom atom : molecule.atoms()) {
// if (atom instanceof IPseudoAtom)
// atom.setSymbol("R");
//
// }
// writer2.write(molecule);
//
// result = stringWriter.toString();
//
// } catch (
//
// InvalidSmilesException e)
//
// {
// throw new CTKSmilesException("invalid smiles", e);
//
// } catch (
//
// CDKException e)
//
// {
// throw new CTKException(e.getMessage(), e);
// } catch (
//
// IOException e)
//
// {
// throw new CTKException(e.getMessage(), e);
// }
//
// return result;
//
// }

// /**
// * @param pseudo
// * @param molecule
// * @param apoBonds
// */
// private void chooseRootAttachmentBonds(IAtom atom, IAtomContainer molecule,
// Map<IAtom, Map<Integer, IBond>> rootAttachmentPoints) {
// int apoIdx = 1;
// Map<Integer, IBond> apoBonds = new HashMap<Integer, IBond>();
// Iterator<IBond> bonds = molecule.bonds().iterator();
// // Pick up to two apo bonds randomly
// while (bonds.hasNext() && apoIdx <= 2) {
// IBond bond = bonds.next();
// if (bond.contains(atom)) {
// apoBonds.put((apoIdx), bond);
// apoIdx++;
// }
// }
// rootAttachmentPoints.put(atom, apoBonds);
//
// }
//
// private String convertExtendedSmiles(String extendedSmiles, List<String> groups) {
//
// char[] parent = extendedSmiles.toCharArray();
// String target = "";
// int index = 1;
// for (char item : parent) {
// if (item == '*') {
// target = target + groups.get(index);
// index++;
// } else
// target = target + item;
//
// }
//
// return target;
//
// }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractChemistryManipulator#getMolecule(java.lang. String,
   * org.helm.chemtoolkit.AttachmentList)
   */
  @Override
  public AbstractMolecule getMolecule(String smiles, AttachmentList attachments) throws CTKException {
    IAtomContainer molecule = getIAtomContainer(smiles);

    return new CDKMolecule(molecule, attachments);
  }

  /**
   * @param smiles
   * @return
   * @throws CTKException
   */
  private IAtomContainer getIAtomContainer(String smiles) throws CTKException {
    IAtomContainer molecule = null;
    smiles = normalize(smiles, getRGroupsFromExtendedSmiles(smiles));
    // LOG.debug("smiles= " + smiles);
    SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
    try {

      molecule = smilesParser.parseSmiles(smiles);
      StructureDiagramGenerator sdg = new StructureDiagramGenerator();
      sdg.setMolecule(molecule);
      sdg.generateCoordinates();
      molecule = sdg.getMolecule();

      for (IAtom atom : molecule.atoms()) {
        if (atom instanceof IPseudoAtom)
          atom.setSymbol("R");

      }

    } catch (CDKException e) {
      throw new CTKException(e.getMessage(), e);
    }
    return molecule;
  }

  @Override
  public IBondBase bindAtoms(IAtomBase atom1, IAtomBase atom2) throws CTKException {
    IBondBase bond = null;
    if ((atom1 instanceof CDKAtom) && (atom1 instanceof CDKAtom)) {

      IBond newBond = new Bond(((CDKAtom) atom1).getMolAtom(), ((CDKAtom) atom2).getMolAtom(), IBond.Order.SINGLE,
          IBond.Stereo.UP);
      // newBond.setStereo(stereo);
      bond = new CDKBond(newBond);

    } else {
      throw new CTKException("invalid atoms");
    }

    return bond;
  }

  @Override
  public AbstractMolecule merge(AbstractMolecule firstMolecule, IAtomBase firstRgroup, AbstractMolecule secondMolecule,
      IAtomBase secondRgroup) throws CTKException {
// if (firstMolecule == secondMolecule) {
// firstMolecule.dearomatize();
// IAtomBase atom1 = getNeighborAtom(firstMolecule, firstRgroup);
// IAtomBase atom2 = getNeighborAtom(secondMolecule, secondRgroup);
//
// IBondBase bond = bindAtoms(atom1, atom2);
// firstMolecule.addIBase(bond);
// } else
//
// {
    firstMolecule.dearomatize();
    secondMolecule.dearomatize();
    IAtomBase atom1 = getNeighborAtom(firstRgroup);
    IAtomBase atom2 = getNeighborAtom(secondRgroup);
    // create a new bond
    IBondBase bond = bindAtoms(atom1, atom2);
    IStereoElement elementToAdd =
        (IStereoElement) getStereoInformation(firstMolecule, firstRgroup, atom1).getStereoElement();
    firstMolecule.removeAttachment(firstRgroup);
    secondMolecule.removeAttachment(secondRgroup);

    firstMolecule.removeINode(firstRgroup);
    secondMolecule.removeINode(secondRgroup);

    firstMolecule.addIBase(secondMolecule);
    firstMolecule.addIBase(bond);
    ((CDKMolecule) firstMolecule).getMolecule().addStereoElement(elementToAdd);

    firstMolecule.setAttachments(mergeAttachments(firstMolecule, firstMolecule.getAttachments(), secondMolecule.getAttachments()));

    return firstMolecule;

  }

  /**
   * @param molecule
   * @param rGroup
   * @param atom
   * @return
   */

  @Override
  public IStereoElementBase getStereoInformation(AbstractMolecule molecule, IAtomBase rGroup, IAtomBase atom) {
    IStereoElement elementToAdd = null;
    for (IStereoElement element : (((CDKMolecule) molecule).getMolecule().stereoElements())) {
      if (element.contains(((CDKAtom) rGroup).getMolAtom())) {
        if (element instanceof ITetrahedralChirality) {
          IAtom[] atomArray = ((ITetrahedralChirality) element).getLigands();
          for (int i = 0; i < atomArray.length; i++) {
            if (atomArray[i].equals(((CDKAtom) rGroup).getMolAtom())) {
              atomArray[i] = (IAtom) atom.getMolAtom();
            }

          }
          elementToAdd =
              new TetrahedralChirality(((ITetrahedralChirality) element).getChiralAtom(), atomArray,
                  (((ITetrahedralChirality) element).getStereo()));
        }
      }
    }
    return new CDKStereoElement(elementToAdd);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws CTKException
   */
  @Override
  public String convertMolecule(AbstractMolecule container, StType type) throws CTKException {
    String result = null;
    IAtomContainer molecule = (IAtomContainer) container.getMolecule();
    switch (type) {
    case SMILES:
      result = molecule2Smiles(molecule);
      break;
    case MOLFILE:
      result = molecule2Molfile(molecule);
    default:
      break;
    }
    return result;
  }

  /**
   * @param molecule
   * @return
   * @throws CTKException
   */
  private String molecule2Molfile(IAtomContainer molecule) throws CTKException {
    String result = null;
    try (StringWriter stringWriter = new StringWriter();
        MDLV2000Writer writer = new MDLV2000Writer(stringWriter)) {

      writer.writeMolecule(molecule);
      result = stringWriter.toString();
    } catch (CDKException e) {
      throw new CTKException("unable to generate coordinates", e);
    } catch (Exception e) {
      throw new CTKException("unable to write molecule", e);

    }
    return result;
  }

}
