import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.BorderFactory;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Vector;
import java.io.FileNotFoundException;


public class ExFrame extends JFrame
{
	/**
	 * @author Nikolay Feldman
	 */
	private static final long serialVersionUID = 1L;
	private File file;
	private ArrayList<Song> songData;
	private JPanel p;
	private JLabel header;
	private JList<String> simpleList;
	private JLabel[] songDetailLabels;//[0]-name, [1]-artist, [2]-album, [3]-year
	private JTextField[] songFields; //[0]-name, [1]-artist, [2]-album, [3]-year
	private JButton[] buttons; //[0]-add, [1]-delete, [2]-edit, [3]-save, [4]-cancel
	public Action action;
	
	public ExFrame()
	{
		//set frame settings
		this.setSize(600, 350);
		this.setTitle("RU Listening?");
		this.setMinimumSize(new Dimension(520,250));
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		//save file on exit
		file = new File("songdata.txt");
		this.addComponentListener(new ComponentAdapter() 
		{
            public void componentHidden(ComponentEvent e) 
            {
                try {
                	FileWriter writer = new FileWriter(file);
                	for(Song song : songData)
                	{
                		String[] details = song.getData();
                		writer.write(details[0]+"\n"+details[1]+"\n"+details[2]+"\n"+details[3]+"\n");
                	}
                	writer.close();
                } catch (Exception e2) { System.out.println("Error saving the song data file!"); }
                ((JFrame)(e.getComponent())).dispose();
            }
        });
		
		
		/*Load up layout */
		p = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		p.setLayout(gridbag);
		
		//load up North - Header Label
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.gridwidth = GridBagConstraints.REMAINDER;
		header = new JLabel("RU Listening? - A Rutgers Audio Library", JLabel.CENTER );
		header.setFont(new Font("SansSerif", Font.BOLD, 22));
		gridbag.setConstraints(header, c);
		p.add(header);
		
		//load up Simple Interface and read data file
		songData = new ArrayList<Song>();
		Vector<String> stringList = readFile_createList();
		simpleList = new JList<String>(stringList);
		Song.setList(stringList);
		simpleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JPanel panelSimple = new JPanel();
		panelSimple.setBorder(BorderFactory.createLineBorder(Color.black));
		JLabel label = new JLabel("Simple Song List", JLabel.CENTER);
		label.setFont(new Font("SansSerif", Font.BOLD, 16));
		panelSimple.setLayout(gridbag);
		c.weighty = 0.001;
		gridbag.setConstraints(label, c);
		c.weighty = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		gridbag.setConstraints(simpleList, c);
		panelSimple.add(label);
		panelSimple.add(simpleList);
		c.weightx = 0.3;
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.gridheight = 2;
		c.weighty = 0.7;
		gridbag.setConstraints(panelSimple, c);
		p.add(panelSimple);
		
		//load up detailed interface
		JPanel panelDetailed = new JPanel();
		JPanel panelDbottom = new JPanel();
		panelDetailed.setLayout(new GridLayout(2,1));
		label = new JLabel("Song Details", JLabel.CENTER);
		label.setFont(new Font("SansSerif", Font.BOLD, 16));
		c.weightx = 0;
		c.weighty = 0.001;
		c.gridwidth = 1;
		c.gridheight = 1;
		gridbag.setConstraints(label, c);
		panelDetailed.add(label);
		panelDbottom.setLayout(new GridLayout(4,2));
		songDetailLabels = new JLabel[4];
		songDetailLabels[0] = new JLabel();
		songDetailLabels[1] = new JLabel();
		songDetailLabels[2] = new JLabel();
		songDetailLabels[3] = new JLabel();
		panelDbottom.add(new JLabel("Song Name:", JLabel.RIGHT)); panelDbottom.add(songDetailLabels[0]);
		panelDbottom.add(new JLabel("Song Artist:", JLabel.RIGHT)); panelDbottom.add(songDetailLabels[1]);
		panelDbottom.add(new JLabel("Song Album:", JLabel.RIGHT)); panelDbottom.add(songDetailLabels[2]);
		panelDbottom.add(new JLabel("Song Year:", JLabel.RIGHT)); panelDbottom.add(songDetailLabels[3]);
		c.weighty = 1;
		gridbag.setConstraints(panelDbottom, c);
		panelDetailed.add(panelDbottom);
		
		c.weightx = 0.7;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(panelDetailed, c);
		p.add(panelDetailed);
		
		
		//Load up edit interface
		songFields = new JTextField[4];
		songFields[0] = new JTextField(20);
		songFields[1] = new JTextField(20);
		songFields[2] = new JTextField(20);
		songFields[3] = new JTextField(20);
		JPanel panelEditInterface = new JPanel(); //outer container
		JPanel panelEdit = new JPanel(); //inner container holding editable fields
		GridBagLayout gridbage = new GridBagLayout();
		GridBagConstraints cel = new GridBagConstraints();
		GridBagConstraints cet = new GridBagConstraints();
		panelEdit.setBorder(BorderFactory.createLineBorder(Color.black));
		panelEdit.setLayout(gridbage);
		panelEditInterface.setLayout(gridbage);
		cel.weightx = 0.3;
		cet.weightx = 1;
		cet.gridwidth = GridBagConstraints.REMAINDER;
		JLabel l1 = new JLabel("Name:"); gridbage.setConstraints(l1, cel); gridbage.setConstraints(songFields[0], cet);
		JLabel l2 = new JLabel("Artist:"); gridbage.setConstraints(l2, cel);gridbage.setConstraints(songFields[1], cet);
		JLabel l3 = new JLabel("Album:"); gridbage.setConstraints(l3, cel);gridbage.setConstraints(songFields[2], cet);
		JLabel l4 = new JLabel("Year:"); gridbage.setConstraints(l4, cel);gridbage.setConstraints(songFields[3], cet);
		panelEdit.add(l1); panelEdit.add(songFields[0]);
		panelEdit.add(l2); panelEdit.add(songFields[1]);
		panelEdit.add(l3); panelEdit.add(songFields[2]);
		panelEdit.add(l4); panelEdit.add(songFields[3]);
	
		buttons = new JButton[5];
		buttons[0] = new JButton("Add Song");
		buttons[1] = new JButton("Delete Song");
		buttons[2] = new JButton("Edit Song");
		buttons[3] = new JButton("Save");
		buttons[4] = new JButton("Cancel");
		JPanel panelButtons = new JPanel();
		//panelButtons.setBorder(BorderFactory.createLineBorder(Color.black));
		panelButtons.setLayout(gridbage);
		GridBagConstraints c2 = new GridBagConstraints();
		gridbage.setConstraints(buttons[0], c2);
		gridbage.setConstraints(buttons[1], c2);
		c2.gridwidth = GridBagConstraints.REMAINDER;
		gridbage.setConstraints(buttons[2], c2);
		c2.gridwidth = 1;
		gridbage.setConstraints(buttons[3], c2);
		gridbage.setConstraints(buttons[4], c2);
		panelButtons.add(buttons[0]);
		panelButtons.add(buttons[1]);
		panelButtons.add(buttons[2]);
		panelButtons.add(buttons[3]);
		panelButtons.add(buttons[4]);

		gridbag.setConstraints(panelEditInterface, c);
		c.weightx = 1;
		c.weighty = 0.8;
		//c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbage.setConstraints(panelEdit, c);
		c.weighty = 0.2;
		gridbage.setConstraints(panelButtons, c);
		panelEditInterface.add(panelEdit);
		panelEditInterface.add(panelButtons);
		p.add(panelEditInterface);
		
		
		this.add(p);
		
		
		action = new Action(songData, simpleList, songDetailLabels, songFields, buttons, panelEdit);
	}
	
	private Vector<String> readFile_createList()
	{
		//Reads file, stores in songData and creates string list to use in JList
		/*TEXTFILE FORMAT
		 * Name
		 * Artist
		 * Album
		 * Year
		 */
		Scanner fileReader;
		try { 
			fileReader = new Scanner(file); 
			while (fileReader.hasNextLine())
			{
				String name = fileReader.nextLine();
				String artist = fileReader.nextLine();
				String album = fileReader.nextLine();
				String year = fileReader.nextLine();
				this.songData.add(new Song(name,artist,album,year));
			}
			fileReader.close();
		} catch (FileNotFoundException e) { System.out.println("File Not Found."); }
		catch (Exception e2) { System.out.println("Invalid text data format."); };

		if (songData.size() == 0)
			return new Vector<String>();
		
		Vector<String> v = new Vector<String>();
		for (int i = 0 ; i < songData.size() ; i++)
		{
			v.add(songData.get(i).getData()[0]);
		} 
		return v;
	}
	

}
