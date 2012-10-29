package cs4620.pa2.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import layout.TableLayout;
import cs4620.pa2.material.ShaderFireMaterial;

public class FireMaterialSettingPanel extends JPanel implements ChangeListener
{
	private static final long	serialVersionUID	= 1L;

	private ShaderFireMaterial material = null;

	private JSpinner s1, s2, s3;
	private boolean changeMaterialValues = true;

	public FireMaterialSettingPanel()
	{
		double[][] tableLayoutSize = {
				{
					5, TableLayout.MINIMUM,
					5, TableLayout.FILL,
					5, TableLayout.FILL,
					5, TableLayout.FILL,
					5
				},
				{
					5, TableLayout.MINIMUM,
					5, TableLayout.MINIMUM,
					5, TableLayout.MINIMUM,
					5, TableLayout.MINIMUM,
					5, TableLayout.MINIMUM,
					5
				}
		};

		TableLayout tableLayout = new TableLayout(tableLayoutSize);
		setLayout(tableLayout);

		setBorder(BorderFactory.createTitledBorder("Phong Material"));

		initLabels();
		initTextFields();
	}

	private void initLabels()
	{
		JLabel RLabel = new JLabel("1");
		add(RLabel, "3, 1, c, c");
		JLabel GLabel = new JLabel("2");
		add(GLabel, "5, 1, c, c");
		JLabel BLabel = new JLabel("3");
		add(BLabel, "7, 1, c, c");

		JLabel color1Label = new JLabel("speed = ");
		add(color1Label, "1, 3, r, c");
	}

	private void initTextFields()
	{
	    s1 = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100.0, 1));
	    s2 = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100.0, 1));
	    s3 = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100.0, 1));
	    
	    add(s1, "3, 3, 3, 3");
	    add(s2, "5, 3, 5, 3");
	    add(s3, "7, 3, 7, 3");
	    
	    s1.addChangeListener(this);
	    s2.addChangeListener(this);
	    s3.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent arg0)
	{
		if (changeMaterialValues && material != null)
		{
			material.scrollSpeeds[0] = ((Double)s1.getValue()).floatValue();
			material.scrollSpeeds[1] = ((Double)s2.getValue()).floatValue();
			material.scrollSpeeds[2] = ((Double)s3.getValue()).floatValue();
		}
	}

	public void setMaterial(ShaderFireMaterial material)
	{
		changeMaterialValues = false;

		this.material = material;

		s1.setValue(new Double(this.material.scrollSpeeds[0]));
		s2.setValue(new Double(this.material.scrollSpeeds[1]));
		s3.setValue(new Double(this.material.scrollSpeeds[2]));

		changeMaterialValues = true;
	}
}