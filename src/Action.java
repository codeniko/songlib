import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.util.ArrayList;

public class Action implements ActionListener, ListSelectionListener
{
	/**
	 * @author Nikolay Feldman
	 */
	
	ArrayList<Song> songData;
	JList<String> simpleList;
	JLabel[] songDetailLabels;//[0]-name, [1]-artist, [2]-album, [3]-year
	JTextField[] songFields; //[0]-name, [1]-artist, [2]-album, [3]-year
	JButton[] buttons; //[0]-add, [1]-delete, [2]-edit, [3]-save, [4]-cancel
	JPanel panelEdit;
	int state;
	
	public Action(ArrayList<Song> songData, JList<String> simpleList, 
			JLabel[] songDetailLabels, JTextField[] 
					songFields, JButton[] buttons, JPanel panelEdit)
	{
		this.songData = songData;
		this.simpleList = simpleList;
		this.songDetailLabels = songDetailLabels;
		this.songFields = songFields;
		this.buttons = buttons;
		this.panelEdit = panelEdit;
		
		if (songData.size() == 0)
		{
			buttons[1].setEnabled(false);
			buttons[2].setEnabled(false);
		}
		
		buttons[3].setEnabled(false);
		buttons[4].setEnabled(false);
		panelEdit.setVisible(false); 
		
		ListSelectionModel lsm = simpleList.getSelectionModel();
		lsm.addListSelectionListener(this);
		buttons[0].addActionListener(this);
		buttons[1].addActionListener(this);
		buttons[2].addActionListener(this);
		buttons[3].addActionListener(this);
		buttons[4].addActionListener(this);
		
		simpleList.setSelectedIndex(0);
	}
	
	private void toggle(boolean makePanelVisible)
	{
		if (makePanelVisible)
		{
			panelEdit.setVisible(true);
			buttons[3].setEnabled(true);
			buttons[4].setEnabled(true);
			buttons[0].setEnabled(false);
			buttons[1].setEnabled(false);
			buttons[2].setEnabled(false);
		} 
		else
		{
			if (songData.size() == 0)
			{
				buttons[1].setEnabled(false);
				buttons[2].setEnabled(false);
			}
			else
			{
				buttons[1].setEnabled(true);
				buttons[2].setEnabled(true);
			}
			panelEdit.setVisible(false);
			buttons[3].setEnabled(false);
			buttons[4].setEnabled(false);
			buttons[0].setEnabled(true);
			songFields[0].setText("");
			songFields[1].setText("");
			songFields[2].setText("");
			songFields[3].setText("");
		}
	}
	
	private void updateDetailLabels(int i)
	{
		if (i == -1)
		{
			songDetailLabels[0].setText("");
			songDetailLabels[1].setText("");
			songDetailLabels[2].setText("");
			songDetailLabels[3].setText("");
		} else
		{
			String[] details = songData.get(i).getData();
			songDetailLabels[0].setText(details[0]);
			songDetailLabels[1].setText(details[1]);
			songDetailLabels[2].setText(details[2]);
			songDetailLabels[3].setText(details[3]);
		}
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		int selection = simpleList.getSelectedIndex();
		String[] details = songData.get(selection).getData();
		songDetailLabels[0].setText(details[0]);
		songDetailLabels[1].setText(details[1]);
		songDetailLabels[2].setText(details[2]);
		songDetailLabels[3].setText(details[3]);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == buttons[0]) //add button clicked
		{
			toggle(true);
			state = -1;
		}
		else if (e.getSource() == buttons[1]) //Delete button clicked
		{
			
				
			int selection = simpleList.getSelectedIndex();
			songData.remove(selection);
			Song.getList().remove(selection);
			simpleList.updateUI();
			if (songData.size() == 0)
			{
				buttons[1].setEnabled(false);
				buttons[2].setEnabled(false);
				updateDetailLabels(-1);
			}
			else
			{
				simpleList.setSelectedIndex(0);
				updateDetailLabels(0);
			}
		}
		else if (e.getSource() == buttons[2]) //Edit button clicked
		{
			toggle(true);
			state = simpleList.getSelectedIndex();
			String[] details = songData.get(state).getData();
			songFields[0].setText(details[0]);
			songFields[1].setText(details[1]);
			songFields[2].setText(details[2]);
			songFields[3].setText(details[3]);
		}
		else if (e.getSource() == buttons[3]) //save button clicked
		{
			if (songFields[0].getText().isEmpty() || songFields[1].getText().isEmpty())
			{
				System.out.println("A new song must have a name and artist!");
				return;
			}
			
			if (state == -1) //adding new song
			{
				for(Song song : songData)
				{
					String[] details = song.getData();
					if (details[0].equalsIgnoreCase(songFields[0].getText()) && 
							details[1].equalsIgnoreCase(songFields[1].getText()))
					{
						System.out.println("A song with the given name and artist already exists!");
						return;
					}
				}
				songData.add(new Song(songFields[0].getText(), songFields[1].getText(),
								songFields[2].getText(), songFields[3].getText() ));
				Song.getList().add(songFields[0].getText());
				simpleList.updateUI();
				simpleList.setSelectedIndex(songData.size()-1);
				updateDetailLabels(songData.size()-1);
			}
			else
			{
				Song song = songData.get(state);
				song.setData(songFields[0].getText(), songFields[1].getText(),
								songFields[2].getText(), songFields[3].getText());
				Song.getList().set(state, songFields[0].getText());
				simpleList.updateUI();
				simpleList.setSelectedIndex(state);
				updateDetailLabels(state);
			}
			toggle(false);
		}
		else if (e.getSource() == buttons[4]) //cancel button clicked
		{
			toggle(false);
		}
	}
}
