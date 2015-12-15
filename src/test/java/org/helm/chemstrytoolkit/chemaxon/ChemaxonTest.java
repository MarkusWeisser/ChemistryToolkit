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
package org.helm.chemstrytoolkit.chemaxon;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.AbstractChemistryManipulator.OutputType;
import org.helm.chemtoolkit.AbstractChemistryManipulator.StType;
import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.Attachment;
import org.helm.chemtoolkit.AttachmentList;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.ManipulatorFactory;
import org.helm.chemtoolkit.ManipulatorFactory.ManipulatorType;
import org.helm.chemtoolkit.MoleculeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author chistyakov
 *
 */
public class ChemaxonTest {

  private static String type = "chemaxon";

  private static final Logger LOG = LoggerFactory.getLogger(ChemaxonTest.class);

  private static AbstractChemistryManipulator getManipulator() {
    // return ChemicalToolKit.getTestINSTANCE(type).getManipulator();
    AbstractChemistryManipulator result = null;

    try {
      result = ManipulatorFactory.buildManipulator(ManipulatorType.MARVIN);
    } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
        | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return result;
  }

  @Test
  void getMoleculeInfoTest() throws CTKException, IOException {
    String smiles = "CCc1nn(C)c2c(=O)[nH]c(nc12)c3cc(ccc3OCC)S(=O)(=O)N4CCN(C)CC4";
    // String smiles = "[*]N1CC[C@H]1C([*])=O |r,$_R2;;;;;;_R1;$|";
    MoleculeInfo moleculeInfo = getManipulator().getMoleculeInfo(getManipulator().getMolecule(smiles, null));
    LOG.debug("exact mass=" + moleculeInfo.getExactMass());
    LOG.debug("molecular weight=" + moleculeInfo.getMolecularWeight());
    LOG.debug("formula=" + moleculeInfo.getMolecularFormula());
    Assert.assertEquals(moleculeInfo.getMolecularFormula(), "C21H28N6O4S");
  }

  @Test
  public void smiles2MolTest() throws CTKException {
    String smiles = "[*]N1CC[C@H]1C([*])=O |r,$_R2;;;;;;_R1;$";
    // String smiles = "[H]C1(OC(CO*)C(O*)C1O)N2C=NC3=C2N=CN=C3N";
    // String result = getManipulator().convertSMILES2MolFile(smiles);
    String result = getManipulator().convert(smiles, StType.SMILES);
    LOG.debug(result);

  }

  @Test
  public void mol2SmilesTest() throws CTKException {
    String molFile = "\n" +
        "  Marvin  11261512352D\n" +
        "\n" +
        " 13 13  0  0  0  0            999 V2000\n" +
        "    1.4617    2.2807    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    0.7479    1.8672    0.0000 C   0  0  1  0  0  0  0  0  0  0  0  0\n" +
        "    2.1768    1.8692    0.0000 C   0  0  2  0  0  0  0  0  0  0  0  0\n" +
        "    0.9625    1.0707    0.0000 C   0  0  2  0  0  0  0  0  0  0  0  0\n" +
        "    1.9644    1.0721    0.0000 C   0  0  1  0  0  0  0  0  0  0  0  0\n" +
        "    0.9637    0.2457    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    1.9656    0.2471    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    0.5994    2.7216    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    2.1756    2.9004    0.0000 R#  0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    0.1279    3.0752    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "   -0.6971    3.2298    0.0000 R#  0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    1.6650   -0.3083    0.0000 R#  0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    2.9949    1.7626    0.0000 H   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "  1  2  1  0  0  0  0\n" +
        "  1  3  1  0  0  0  0\n" +
        "  2  4  1  0  0  0  0\n" +
        "  2  8  1  1  0  0  0\n" +
        "  3  5  1  0  0  0  0\n" +
        "  3  9  1  1  0  0  0\n" +
        "  4  5  1  0  0  0  0\n" +
        "  4  6  1  6  0  0  0\n" +
        "  5  7  1  6  0  0  0\n" +
        "  6 12  1  0  0  0  0\n" +
        "  8 10  1  0  0  0  0\n" +
        " 10 11  1  0  0  0  0\n" +
        "  3 13  1  6  0  0  0\n" +
        "M  RGP  3   9   3  11   1  12   2\n" +
        "M  END";

    // String result = getManipulator().convertMolFile2SMILES(molFile);
    String result = getManipulator().convert(molFile, StType.MOLFILE);
    LOG.debug(result);
    result = getManipulator().canonicalize(result);
    LOG.debug(result);
// Assert.assertEquals(result, "CCOc1ccc(cc1-c1nc2c(CC)nn(C)c2c(=O)[nH]1)S(=O)(=O)N1CCN(C)CC1");

  }

  @Test
  public void canonicalizeTest() throws CTKSmilesException, CTKException {
    String smiles = "CCc1nn(C)c2c(=O)[nH]c(nc12)c3cc(ccc3OCC)S(=O)(=O)N4CCN(C)CC4";
    String result = getManipulator().canonicalize(smiles);
    LOG.debug(result);
    result = getManipulator().canonicalize(smiles);
    LOG.debug(result);

  }

  @Test
  public void renderMolTest() throws CTKException, IOException {
    String molFile = "\n" + "  ACCLDraw11131512172D\n" + "\n" + " 32 35  0  0  0  0  0  0  0  0999 V2000\n"
        + "    7.6862   -7.0367    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    6.6485   -6.4506    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    5.6213   -4.6864    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    6.6485   -5.2695    0.0000 N   0  0  3  0  0  0  0  0  0  0  0  0\n"
        + "    7.6653   -4.6687    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    8.7132   -5.2548    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    8.7132   -6.4417    0.0000 N   0  0  3  0  0  0  0  0  0  0  0  0\n"
        + "   10.3235   -5.9976    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    9.1573   -8.0519    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    9.7404   -7.0246    0.0000 S   0  0  3  0  0  0  0  0  0  0  0  0\n"
        + "   15.8922   -9.3677    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   14.8676   -8.7801    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   13.8465   -9.3736    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   12.8167   -8.7860    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   11.7999   -9.3781    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   10.7675   -8.7950    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   10.7675   -7.6078    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   11.7895   -7.0036    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   12.8167   -7.5988    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   15.8892   -7.0083    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   14.8640   -7.5949    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   13.8388   -7.0068    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   13.8388   -5.8189    0.0000 N   0  0  3  0  0  0  0  0  0  0  0  0\n"
        + "   14.8640   -4.0379    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   14.8640   -5.2190    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   15.8892   -5.8189    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   17.3854   -4.3283    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   17.0205   -5.4515    0.0000 N   0  0  3  0  0  0  0  0  0  0  0  0\n"
        + "   17.7197   -6.4137    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   17.0205   -7.3760    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   17.3853   -8.4994    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   18.5407   -8.7449    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" + "  2  1  1  0  0  0  0\n"
        + "  4  2  1  0  0  0  0\n" + "  4  3  1  0  0  0  0\n" + "  5  4  1  0  0  0  0\n"
        + "  6  5  1  0  0  0  0\n" + "  7  6  1  0  0  0  0\n" + "  7  1  1  0  0  0  0\n"
        + " 10  7  1  0  0  0  0\n" + " 10  8  2  0  0  0  0\n" + " 10  9  2  0  0  0  0\n"
        + " 17 10  1  0  0  0  0\n" + " 12 11  1  0  0  0  0\n" + " 13 12  1  0  0  0  0\n"
        + " 14 13  1  0  0  0  0\n" + " 15 14  1  0  0  0  0\n" + " 16 15  2  0  0  0  0\n"
        + " 17 16  1  0  0  0  0\n" + " 18 17  2  0  0  0  0\n" + " 19 18  1  0  0  0  0\n"
        + " 19 14  2  0  0  0  0\n" + " 22 19  1  0  0  0  0\n" + " 21 20  1  0  0  0  0\n"
        + " 22 21  2  0  0  0  0\n" + " 23 22  1  0  0  0  0\n" + " 25 23  1  0  0  0  0\n"
        + " 25 24  2  0  0  0  0\n" + " 26 25  1  0  0  0  0\n" + " 26 20  2  0  0  0  0\n"
        + " 28 26  1  0  0  0  0\n" + " 28 27  1  0  0  0  0\n" + " 29 28  1  0  0  0  0\n"
        + " 30 29  2  0  0  0  0\n" + " 30 20  1  0  0  0  0\n" + " 31 30  1  0  0  0  0\n"
        + " 32 31  1  0  0  0  0\n" + "M  END";
    byte[] result = getManipulator().renderMol(molFile, OutputType.PNG, 1000, 1000, (int) Long.parseLong("D3D3D3", 16));
    try (FileOutputStream out = new FileOutputStream("test-output\\ChemaxonTestbild.png")) {
      out.write(result);
    }

  }

  /*
   * @Test public void convertSeq2MolFileTest() throws CTKException { String sequence = "GGT"; String result =
   * getManipulator().convert(sequence, InputType.SEQUENCE); LOG.debug(result); }
   */

  @Test
  public void getExactMoleculeInfo() throws IOException, CTKException {
    AttachmentList groups = new AttachmentList();
    groups.add(new Attachment("R2-OH", "R2", "OH", "O[*] |$;_R2$|"));
    groups.add(new Attachment("R1-H", "R1", "H", "[*][H] |$_R1;$|"));
    AbstractChemistryManipulator manipulator = getManipulator();
    AbstractMolecule molecule = manipulator.getMolecule("[*]N1CC[C@H]1C([*])=O |r,$_R2;;;;;;_R1;$|", groups);
    Map<AbstractMolecule, Map<IAtomBase, IAtomBase>> groupsToMerge = new HashMap<>();
    for (Attachment attachment : molecule.getAttachments()) {
      int groupId = AbstractMolecule.getIdFromLabel(attachment.getLabel());
      String smiles = attachment.getSmiles();
      LOG.debug(smiles);
      AbstractMolecule rMol = manipulator.getMolecule(smiles, null);
      Map<IAtomBase, IAtomBase> atoms = new HashMap<>();
      atoms.put(molecule.getRGroupAtom(groupId, true), rMol.getRGroupAtom(groupId, true));
      groupsToMerge.put(rMol, atoms);
    }
    for (AbstractMolecule mol : groupsToMerge.keySet()) {
      for (IAtomBase atom : groupsToMerge.get(mol).keySet()) {
        molecule =
            manipulator.merge(molecule, atom, mol, groupsToMerge.get(mol).get(atom));
      }
    }
    MoleculeInfo moleculeInfo =
        manipulator.getMoleculeInfo(molecule);
    LOG.debug("exact mass=" + moleculeInfo.getExactMass());
    LOG.debug("molecular weight=" + moleculeInfo.getMolecularWeight());
    LOG.debug("formula=" + moleculeInfo.getMolecularFormula());
    molecule.dearomatize();
    molecule.generateCoordinates();

    String result = manipulator.convertMolecule(molecule, StType.MOLFILE);
    LOG.debug(result);

  }

  @Test(groups = {"MarvinTest"})
  public void adeninRiboseMerge() throws IOException, CTKException {

    String ribose = "[H][C@@]1([*])O[C@H](CO[*])[C@@H](O[*])[C@H]1O |$;;_R3;;;;;_R1;;;_R2;;$|";

    String adenin = "[*]n1cnc2c1ncnc2N |r,$_R1;;;;;;;;;;;;$|";

    String riboseR1 = "[*][H] |$_R1;$|";
    String riboseR2 = "[*][H] |$_R2;$|";
    String riboseR3 = "O[*] |$;_R3$|";
    String adeninR1 = "[*][H] |$_R1;$|";
    AbstractChemistryManipulator manipulator = getManipulator();
    AttachmentList groupsAdenin = new AttachmentList();
    AttachmentList groupsRibose = new AttachmentList();
    groupsAdenin.add(new Attachment("R1-H", "R1", "H", adeninR1));
    groupsRibose.add(new Attachment("R1-H", "R1", "H", riboseR1));
    groupsRibose.add(new Attachment("R2-H", "R2", "H", riboseR2));
    groupsRibose.add(new Attachment("R3-OH", "R3", "OH", riboseR3));

    AbstractMolecule adeninMolecule = manipulator.getMolecule(adenin, groupsAdenin);

    AbstractMolecule riboseMolecule = manipulator.getMolecule(ribose, groupsRibose);

    for (IAtomBase atom : riboseMolecule.getIAtomArray()) {
      LOG.debug("rGroup atom=" + atom.getRgroup());

    }

    AbstractMolecule molecule =
        manipulator.merge(riboseMolecule, riboseMolecule.getRGroupAtom(3, true), adeninMolecule, adeninMolecule.getRGroupAtom(1, true));
    molecule.dearomatize();
    molecule.generateCoordinates();
    String result = manipulator.convertMolecule(molecule, StType.MOLFILE);
    LOG.debug(result);

    //

    for (IAtomBase atom : molecule.getIAtomArray()) {
      LOG.debug("rGroup atom=" + atom.getRgroup());

    }

    LOG.debug("atom" + molecule.getRGroupAtom(1, false).getMolAtom());
    LOG.debug("atom" + molecule.getRGroupAtom(2, false).getMolAtom());
  }

  @Test(groups = {"MarvinTest"})
  public void mergePhosphatRiboseTest() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException,
      CTKException {

    String ribose = "O[C@H]1[C@H]([*])O[C@H](CO[*])[C@H]1O[*] |$;;;_R3;;;;;_R1;;;_R2$|";
    String phosphat = "OP([*])([*])=O |$;;_R1;_R2;$|";
    String riboseR1 = "[*][H] |$_R1;$|";
    String riboseR2 = "[*][H] |$_R2;$|";
    String riboseR3 = "O[*] |$;_R3$|";
    String phosphatR1 = "O[*] |$;_R1$|";
    String phosphatR2 = "O[*] |$;_R2$|";
    AttachmentList groupsRibose = new AttachmentList();
    groupsRibose.add(new Attachment("R3-OH", "R3", "OH", riboseR3));
    groupsRibose.add(new Attachment("R1-H", "R1", "H", riboseR1));
    groupsRibose.add(new Attachment("R2-H", "R2", "H", riboseR2));

    for (Attachment attachment : groupsRibose) {

      LOG.debug("id=" + attachment.getId());
      LOG.debug("name=" + attachment.getName());
      LOG.debug("label=" + attachment.getLabel());
      LOG.debug("smiles=" + attachment.getSmiles());

    }
    LOG.debug("after sort:");
    for (Attachment attachment : groupsRibose.cloneList()) {

      LOG.debug("id=" + attachment.getId());
      LOG.debug("name=" + attachment.getName());
      LOG.debug("label=" + attachment.getLabel());
      LOG.debug("smiles=" + attachment.getSmiles());
    }
    AttachmentList groupsPhosphat = new AttachmentList();
    groupsPhosphat.add(new Attachment("R1-H", "R1", "OH", phosphatR1));
    groupsPhosphat.add(new Attachment("R2-H", "R2", "OH", phosphatR2));

    AbstractChemistryManipulator manipulator = ManipulatorFactory.buildManipulator(ManipulatorType.MARVIN);
    AbstractMolecule riboseMolecule = manipulator.getMolecule(ribose, groupsRibose);
    AbstractMolecule phosphatMolecule = manipulator.getMolecule(phosphat, groupsPhosphat);
    AbstractMolecule molecule =
        manipulator.merge(riboseMolecule, riboseMolecule.getRGroupAtom(2, true), phosphatMolecule, phosphatMolecule.getRGroupAtom(1, true));

    for (IAtomBase atom : molecule.getIAtomArray()) {
      LOG.debug("rGroup atom=" + atom.getRgroup());

    }
    LOG.debug("atom" + molecule.getRGroupAtom(1, false).getMolAtom());
    LOG.debug("atom" + molecule.getRGroupAtom(2, false).getMolAtom());
    LOG.debug("atom" + molecule.getRGroupAtom(3, false).getMolAtom());
    LOG.debug("atom R" + molecule.getRGroupAtom(1, true).getMolAtom());
    LOG.debug("atom R" + molecule.getRGroupAtom(2, true).getMolAtom());
    LOG.debug("atom R" + molecule.getRGroupAtom(3, true).getMolAtom());

    LOG.debug(manipulator.convertMolecule(molecule, StType.MOLFILE));

    for (Attachment attachment : molecule.getAttachments()) {

      LOG.debug("id=" + attachment.getId());
      LOG.debug("name=" + attachment.getName());
      LOG.debug("label=" + attachment.getLabel());
      LOG.debug("smiles=" + attachment.getSmiles());
    }
  }

  @Test(groups = {"MarvinTest"})
  public void mergeTest() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException,
      CTKException {
    String molecule1 = "[*]N[C@@H](CC([*])=O)C([*])=O |$_R1;;;;;_R3;;;_R2;$|";
    String molecule2 = "[*]N[C@@H](CCC([*])=O)C([*])=O |$_R1;;;;;;_R3;;;_R2;$|";
    String mol1Gruppe1 = "[*][H] |$_R1;$|";
    String mol1Gruppe2 = "O[*] |$_R2;$|";
    String mol1Gruppe3 = "O[*] |$;_R3$|";
    String mol2Gruppe1 = "[*][H] |$_R1;$|";
    String mol2Gruppe2 = "O[*] |$_R2;$|";
    String mol2Gruppe3 = "O[*] |$;_R3$|";

    AttachmentList mol1Attachments = new AttachmentList();
    mol1Attachments.add(new Attachment("R3-OH", "R3", "OH", mol1Gruppe3));
    mol1Attachments.add(new Attachment("R1-H", "R1", "H", mol1Gruppe1));
    mol1Attachments.add(new Attachment("R2-OH", "R2", "OH", mol1Gruppe2));

    AttachmentList mol2Attachments = new AttachmentList();
    mol2Attachments.add(new Attachment("R3-OH", "R3", "OH", mol2Gruppe3));
    mol2Attachments.add(new Attachment("R1-H", "R1", "H", mol2Gruppe1));
    mol2Attachments.add(new Attachment("R2-OH", "R2", "OH", mol2Gruppe2));
    AbstractChemistryManipulator manipulator = getManipulator();
    AbstractMolecule absMol1 = manipulator.getMolecule(molecule1, mol1Attachments);
    AbstractMolecule absMol2 = manipulator.getMolecule(molecule2, mol2Attachments);
    AbstractMolecule mergedMolecule =
        manipulator.merge(absMol1, absMol1.getRGroupAtom(2, true), absMol2, absMol2.getRGroupAtom(1, true));

    String result = manipulator.convertMolecule(mergedMolecule, StType.MOLFILE);
    LOG.debug(result);

    for (Attachment attachment : mergedMolecule.getAttachments()) {
      LOG.debug("label=" + attachment.getLabel());

    }

  }

  @Test(groups = {"MarvinTest"})
  public void getRibose() throws IOException, CTKException {
    String ribose = "O[C@H]1[C@H]([*])O[C@H](CO[*])[C@H]1O[*] |$;;;_R3;;;;;_R1;;;_R2$|";

    String riboseR1 = "[*][H] |$_R1;$|";
    String riboseR2 = "[*][H] |$_R2;$|";
    String riboseR3 = "O[*] |$;_R3$|";

    AttachmentList groupsRibose = new AttachmentList();
    groupsRibose.add(new Attachment("R3-OH", "R3", "OH", riboseR3));
    groupsRibose.add(new Attachment("R1-H", "R1", "H", riboseR1));
    groupsRibose.add(new Attachment("R2-H", "R2", "H", riboseR2));
    AbstractChemistryManipulator manipulator = getManipulator();
    AbstractMolecule molecule = manipulator.getMolecule(ribose, groupsRibose);
    Map<AbstractMolecule, Map<IAtomBase, IAtomBase>> groupsToMerge = new HashMap<>();
    for (Attachment attachment : molecule.getAttachments()) {
      int groupId = AbstractMolecule.getIdFromLabel(attachment.getLabel());
      String smiles = attachment.getSmiles();
      LOG.debug(smiles);
      AbstractMolecule rMol = manipulator.getMolecule(smiles, null);
      Map<IAtomBase, IAtomBase> atoms = new HashMap<>();
      atoms.put(molecule.getRGroupAtom(groupId, true), rMol.getRGroupAtom(groupId, true));
      groupsToMerge.put(rMol, atoms);
    }
    for (AbstractMolecule mol : groupsToMerge.keySet()) {
      for (IAtomBase atom : groupsToMerge.get(mol).keySet()) {
        molecule =
            manipulator.merge(molecule, atom, mol, groupsToMerge.get(mol).get(atom));
      }
    }
    molecule.generateCoordinates();
    String result = manipulator.convertMolecule(molecule, StType.MOLFILE);
    LOG.debug("result=" + result);
  }
}