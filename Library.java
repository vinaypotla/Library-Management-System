
package com.library.management;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;



import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

import com.library.management.Db_Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class Library {

	private JFrame frame;
	 
	//Search Pane
	private JTextField txtSearch;
	
	//Check In Pane
	private JTextField txtCnIsbn;
	private JTextField txtCnCard;
	private JTextField txtCnName;
	private JTable table_1;
	
	//Check Out Pane
	private JTextField txtCoCard;
	private JTextField txtCoIsbn;
	
	//Add Borrower
	private JTextField txtFname;
	private JTextField txtLname;
	private JTextField txtSSN;
	
	//Fines Pane
	private JTextField txtFinesCard;
	private JTable tblSearch;
	private JTable tblChkFines;
	private JTextField txtEmail;
	private JTextField txtCnPhone;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Library window = new Library();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Library() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1331, 737);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		
		//Search Panel
		
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Search", null, panel, null);
		panel.setLayout(null);
		
		txtSearch = new JTextField();
		txtSearch.setBackground(Color.WHITE);
		txtSearch.setForeground(Color.BLACK);
		txtSearch.setBounds(42, 16, 617, 61);
		panel.add(txtSearch);
		txtSearch.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try 
				{
					DefaultTableModel model3 = (DefaultTableModel) tblSearch.getModel();
					model3.setRowCount(0);
					String search = txtSearch.getText();
					Db_Connection con = new Db_Connection();
					 
					String query= null;
					 
					
					String book_id = null, book_title = null, author_name = null;
					boolean book_availability = false;
					
					if(!search.isEmpty())
					{
						query="select b.isbn13, b.title, a.author, b.availability from book b, BOOK_AUTHORS ba, authors a where b.isbn13=ba.isbn13 and ba.author_id=a.author_id and (b.isbn13 like '%"+ search +"%' or a.author like '%"+ search +"%' or b.title  like '%"+ search +"%' ) order by b.isbn13";
						
						if(!query.isEmpty()){  						
				            
				              
				            
				            ResultSet r = con.Connection(query);  // database implementation
				            
				            if(!r.next()){
	
				            	JOptionPane.showMessageDialog(null,"No books");
				            }
				            else{
				            	Object obj1[] = {book_id,book_title,author_name,book_availability};
				                do {
				                	book_id = r.getString("isbn13");
				                	book_title = r.getString("title");
				                	author_name = r.getString("author");
				                	book_availability = r.getBoolean("availability");
				                	if(book_id.equals(obj1[0])) {
				                		obj1[2] = obj1[2] + ", " + author_name;
				                	} else {
				                		if(obj1[0]!=null)
				                			model3.addRow(obj1);
				                		obj1[0] = book_id;
			                			obj1[1] = book_title;
			                			obj1[2] = author_name;
			                			obj1[3] = book_availability;
				                	}
				                } while (r.next());
				            }
						}
					}
					else 
						{
							JOptionPane.showMessageDialog(null,"Enter any value to search");
						}
				}
				catch(SQLException e4)
				{
					e4.printStackTrace();
				}
				
			}
		});
		btnSearch.setBounds(718, 15, 287, 62);
		panel.add(btnSearch);
		
		tblSearch = new JTable();
		tblSearch.setBounds(42, 123, 1229, 483);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(42, 115, 1229, 532);
		panel.add(scrollPane);
		
		tblSearch.setModel(new DefaultTableModel
				(
			new Object[][] {
			},
			new String[] {
				"ISBN", "Book Title", "Book Authors", "Book availability"
			}
						)
				);
		scrollPane.setViewportView(tblSearch);
		
		
		//Check Out Panel
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Check Out", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblCardNumber_1 = new JLabel("Card Number * :");
		lblCardNumber_1.setBounds(29, 33, 162, 20);
		panel_2.add(lblCardNumber_1);
		
		txtCoCard = new JTextField();
		txtCoCard.setBounds(187, 30, 237, 26);
		panel_2.add(txtCoCard);
		txtCoCard.setColumns(10);
		
		JLabel lblIsbn_1 = new JLabel("ISBN * :");
		lblIsbn_1.setBounds(548, 33, 69, 20);
		panel_2.add(lblIsbn_1);
		
		txtCoIsbn = new JTextField();
		txtCoIsbn.setBounds(643, 30, 292, 26);
		panel_2.add(txtCoIsbn);
		txtCoIsbn.setColumns(10);
		
		JButton btnCheckOut = new JButton("Check Out");
		btnCheckOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String card = txtCoCard.getText();
				String isbn = txtCoIsbn.getText();
				
				Db_Connection con = new Db_Connection(); 
				
				if(!card.isEmpty() || !isbn.isEmpty())
				{
					String q1,q2,q3,q4,q5;
					boolean availability;
					
					q1 = "Select * from book where isbn13 = '" + isbn + "'";
					q2 = "select * from borrowers where card_id = '"+ card + "'";
					q3 = "SELECT count(loan_id) FROM book_loans where card_id = '"+ card +"' and date_in is null";
					
					ResultSet r1 = con.Connection(q1); 
					ResultSet r2 = con.Connection(q2);
					ResultSet r3 = con.Connection(q3);
					
					try {
						if(r1.next() && r2.next())
						{
							if(r3.next())
							{
								String count = r3.getString(1);
								int count1 = Integer.parseInt(count);
								if(count1<3)
								{
									availability = r1.getBoolean("Availability");
									if(availability){
										 q4 = "insert into book_loans (isbn13,card_id,date_out,due_date)" +
										          "values ('"+ isbn +"', '"+ card +"', curdate(), DATE_ADD(date_out,INTERVAL 14 DAY))";
										 
										 q5 = "UPDATE book SET Availability = 0 WHERE isbn13='"+ isbn +"'";
										 con.Connection1(q4);
										 con.Connection1(q5);
										 JOptionPane.showMessageDialog(null,"New book_loan is created"); 
								
									}
									else
									{
										JOptionPane.showMessageDialog(null,"Book not available");
									}
						
								}
								else 
								{
									JOptionPane.showMessageDialog(null,"User Borrowed 3 books");
								}
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Please Check your credentials");
						}
					 } 
					 catch (Exception e1) 
					 {
							
							e1.printStackTrace();
					 }	
				}
				else if (card.isEmpty() && !isbn.isEmpty()){
					
					
	            	JOptionPane.showMessageDialog(null,"Please Enter ISBN");
					
				}
				else if (!isbn.isEmpty() && card.isEmpty()){
					
					
	            	JOptionPane.showMessageDialog(null,"Please Enter Card No");
					
				}
				else{
					
	            	JOptionPane.showMessageDialog(null,"Please Enter the required fields");
				}
			}
		});
		btnCheckOut.setBounds(979, 29, 203, 29);
		panel_2.add(btnCheckOut);
		
		
		
		//Check In Panel
		
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Check In", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblIsbn = new JLabel("ISBN:");
		lblIsbn.setBounds(34, 33, 69, 20);
		panel_1.add(lblIsbn);
		
		txtCnIsbn = new JTextField();
		txtCnIsbn.setBounds(118, 30, 268, 26);
		panel_1.add(txtCnIsbn);
		txtCnIsbn.setColumns(10);
		
		JLabel lblCardNumber = new JLabel("Card Number:");
		lblCardNumber.setBounds(443, 33, 112, 20);
		panel_1.add(lblCardNumber);
		
		txtCnCard = new JTextField();
		txtCnCard.setBounds(592, 30, 126, 26);
		panel_1.add(txtCnCard);
		txtCnCard.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(790, 33, 69, 20);
		panel_1.add(lblName);
		
		txtCnName = new JTextField();
		txtCnName.setBounds(897, 30, 308, 26);
		panel_1.add(txtCnName);
		txtCnName.setColumns(10);
		
		JButton btnCnSearch = new JButton("Check For Loans");
		btnCnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				DefaultTableModel model3 = (DefaultTableModel) table_1.getModel();
				model3.setRowCount(0);
				
				String isbn = txtCnIsbn.getText();
				String card = txtCnCard.getText();
				String name = txtCnName.getText();
				
				
				try{
									
					Db_Connection con = new Db_Connection();
					String query;
					
					
					if(!isbn.isEmpty()||!name.isEmpty()||!card.isEmpty()){
						
						if(!isbn.isEmpty()&&!card.isEmpty()&&!name.isEmpty()) {
							query = "select * from book_loans where isbn13='"+ isbn +"'";
						}
						
					else if(!isbn.isEmpty()&&!card.isEmpty()&&!name.isEmpty()){
							query = "select * from book_loans where isbn13='"+ isbn +"'";
						}
						else if(!isbn.isEmpty() && !card.isEmpty() && name.isEmpty()){
							query = "select * from book_loans where isbn13='"+ isbn +"'";
						}
						else if(!isbn.isEmpty() && card.isEmpty() && !name.isEmpty()){
							query = "select * from book_loans where isbn13='"+ isbn +"'";
						}
						else if(isbn.isEmpty() && !card.isEmpty() && !name.isEmpty()){
							query = "Select * from book_loans where card_id='"+ card +"'";
						}
						else if(isbn.isEmpty() && !card.isEmpty() && name.isEmpty()){
							query = "Select * from book_loans where card_id='"+ card +"'";
						}
						else if(!isbn.isEmpty() && card.isEmpty() && name.isEmpty()){
							query = "select * from book_loans where isbn13='"+ isbn +"'";
						}
						
				
						else{
							Db_Connection con1 = new Db_Connection();
							/*
							String q1;
							q1 = "Select card_id from borrowers where bname like '%"+name+"%'";
							ResultSet r9 = con1.Connection(q1);
							System.out.println(r9.first());
							String c = r9.getString(1);
							int count1 = Integer.parseInt(c);
							*/
							query = "select * from book_loans where card_id IN ( Select card_id from borrowers where bname like '%"+name+"%');";
							ResultSet r = con1.Connection(query);
						}
						
							
						
						Db_Connection con1 = new Db_Connection();
						ResultSet r = con1.Connection(query);
						
						if(!r.next()){
							
							JOptionPane.showMessageDialog(null,"No book loans exists");
							
							
						}
						else{
							do{
								
								String id = r.getString("isbn13");
								String date_in= r.getString("date_in");
								String date_out = r.getString("date_out");
								String due_date= r.getString("due_date");
								String card_number = r.getString("card_id");
								
								String loan_id = r.getString("loan_id");
								

								
								Object obj1[] = {loan_id,id,card_number,date_in,date_out,due_date}; 
								model3.addRow(obj1);
							}while(r.next());
							
							
						}
						
						
					}
					else{
						JOptionPane.showMessageDialog(null,"Enter Values");
					}
					
						}
				catch(Exception ex){
					ex.printStackTrace();
				}

			}
			
		});
		btnCnSearch.setBounds(354, 72, 501, 29);
		panel_1.add(btnCnSearch);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(34, 130, 980, 380);
		panel_1.add(scrollPane_1);
		
		table_1 = new JTable();
		table_1.setModel(new DefaultTableModel
				(
			new Object[][] {
			},
			new String[] {
				"LOAN ID", "ISBN", "CARD ID", "DATE IN","DATE OUT","DUE DATE"
			}
						)
				);
		scrollPane_1.setViewportView(table_1);
				
		JButton btnCheckIn = new JButton("Check In");
		btnCheckIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int rowIndex=table_1.getSelectedRow();
				Db_Connection con = new Db_Connection();
				if(rowIndex == -1){
					
					JOptionPane.showMessageDialog(null,"select atleast a single row");
					
				}
				else{
					String loan_id = (String) table_1.getValueAt(rowIndex, 0);
					String isbn = (String) table_1.getValueAt(rowIndex, 1);
					String card = (String)table_1.getValueAt(rowIndex, 2);
					
			        try{
			        	String query4="select datediff(date_in,due_date) as diff from book_loans where isbn13 = '"+isbn+"';";
			            
			        	
			            String query1= "update book_loans set date_in = curdate() where loan_id = '"+loan_id+"'";
			            String query2= "UPDATE book SET availability =1 WHERE isbn13='"+isbn+"'";
			            con.Connection1(query1);
			            con.Connection1(query2);
			            
			          ResultSet r11 = con.Connection(query4);
				           
			            	if(r11.next())    {
			            		String c = r11.getString("diff");
						           int x1 = Integer.parseInt(c);
								
						           if(x1 > 0)
						            {
						            
						            float y = (float) (x1 * 0.25);
						           String query3= "insert into fines (loan_id,fine_amt,paid) values ('"+loan_id+"','"+y+"',0)";
						           con.Connection1(query3);
						           
						          }
			            
			        	
			           }
			        JOptionPane.showMessageDialog(null,"Check in data is updated");
			        }
			        catch (Exception e9) {
			            e9.printStackTrace();
			        }        
			        
					
				}
				
			
			
			
			}
		});
		btnCheckIn.setBounds(354, 584, 400, 29);
		panel_1.add(btnCheckIn);
		
		
		
		//Add Borrowers Panel
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Add Borrower", null, panel_3, null);
		panel_3.setLayout(null);
		
		JLabel lblAllFieldsAre = new JLabel("All fields are mandatory");
		lblAllFieldsAre.setForeground(Color.RED);
		lblAllFieldsAre.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblAllFieldsAre.setBounds(43, 29, 254, 62);
		panel_3.add(lblAllFieldsAre);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setBounds(43, 137, 160, 20);
		panel_3.add(lblFirstName);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setBounds(43, 210, 119, 20);
		panel_3.add(lblLastName);
		
		JLabel lblSsn = new JLabel("SSN");
		lblSsn.setBounds(43, 279, 69, 20);
		panel_3.add(lblSsn);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setBounds(43, 343, 69, 20);
		panel_3.add(lblAddress);
		
		txtFname = new JTextField();
		txtFname.setBounds(181, 134, 355, 26);
		panel_3.add(txtFname);
		txtFname.setColumns(10);
		
		txtLname = new JTextField();
		txtLname.setBounds(177, 207, 359, 26);
		panel_3.add(txtLname);
		txtLname.setColumns(10);
		
		txtSSN = new JTextField();
		txtSSN.setBounds(181, 276, 355, 26);
		panel_3.add(txtSSN);
		txtSSN.setColumns(10);
		
		JLabel lblEmailAddress = new JLabel("Email Address");
		lblEmailAddress.setBounds(620, 137, 138, 20);
		panel_3.add(lblEmailAddress);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(790, 134, 399, 26);
		panel_3.add(txtEmail);
		txtEmail.setColumns(10);
		
		JLabel lblPhoneNumber = new JLabel("Phone Number");
		lblPhoneNumber.setBounds(620, 210, 138, 20);
		panel_3.add(lblPhoneNumber);
		
		txtCnPhone = new JTextField();
		txtCnPhone.setBounds(790, 207, 399, 26);
		panel_3.add(txtCnPhone);
		txtCnPhone.setColumns(10);
		
		JTextArea txtAddress = new JTextArea();
		txtAddress.setBounds(181, 343, 355, 209);
		panel_3.add(txtAddress);
		
		JButton btnAddBorrower = new JButton("Add Borrower");
		btnAddBorrower.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String s1 = txtFname.getText();
				String s2 = txtLname.getText();
				String s3 = txtSSN.getText();
				String s4 = txtEmail.getText();
				String s5 = txtCnPhone.getText();
				String s6 = txtAddress.getText();
				
				String name =s1+" "+s2;
				
				try{
					if(!s1.isEmpty() && !s2.isEmpty() && !s3.isEmpty() && !s4.isEmpty() && !s5.isEmpty() && !s6.isEmpty()){
						if(s5.matches("^(\\([0-9]{3}\\)|[0-9]{3}-)[0-9]{3}-[0-9]{4}$"))
						{
							//String query1 = "select count(*) from borrowers where email ='" + s4 + "';";
							String query1 = "select count(*) from borrowers where ssn ='" + s3 + "';";
							Db_Connection con  = new Db_Connection();
							ResultSet r = con.Connection(query1);
						
						if(r.next()){
							
							String count = r.getString(1);
							int c = Integer.parseInt(count);
							
							if(c==0)
							{							
								String query2 = "Insert into borrowers(ssn,bname,email,address,phone) values ('"+s3+"','"+name+"','"+s4+"','"+s6+"','"+s5+"');";
								int r1 = con.Connection1(query2);
								
								if(r1!=0)
								{
									JOptionPane.showMessageDialog(null,"New Borrower is created");
								}
								
							}
							
							else{
								JOptionPane.showMessageDialog(null,"SSN already Exists");
								
							}
						}
						}
						else{
							JOptionPane.showMessageDialog(null,"Enter a valid phone number");
							
						}
						}
						
					
						else{
							JOptionPane.showMessageDialog(null,"All required fields must be filled");
						
						}
				}catch(SQLException e){
					
					e.printStackTrace();
				}
				
			}
		});
		btnAddBorrower.setBounds(345, 576, 236, 29);
		panel_3.add(btnAddBorrower);
		
		JButton btnResetDetails = new JButton("Reset Details");
		btnResetDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				txtFname.setText("");
				txtLname.setText("");
				txtFname.setText("");
				txtSSN.setText("");
				txtEmail.setText("");
				txtCnPhone.setText("");
				txtAddress.setText("");
			}
		});
		btnResetDetails.setBounds(790, 576, 236, 29);
		panel_3.add(btnResetDetails);
		
		
		
		//Fines Panel
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Check Fines", null, panel_4, null);
		panel_4.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Card Number");
		lblNewLabel.setBounds(37, 27, 197, 20);
		panel_4.add(lblNewLabel);
		
		txtFinesCard = new JTextField();
		txtFinesCard.setBounds(155, 21, 218, 26);
		panel_4.add(txtFinesCard);
		txtFinesCard.setColumns(10);
		
		JCheckBox chckbxShowPreviouslyPaid = new JCheckBox("Show Previously Paid");
		chckbxShowPreviouslyPaid.setBounds(623, 23, 210, 29);
		panel_4.add(chckbxShowPreviouslyPaid);
		
		JButton btnPayFine = new JButton("Pay Fine");
		btnPayFine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String query=null;
				 Db_Connection conn = new Db_Connection();
				 
				 int rowIndex = tblChkFines.getSelectedRow();
				 if(rowIndex < 0){
					 JOptionPane.showMessageDialog(null, "Please select a row");
				 }
				 
				 else{
				 String book_returned = (String) tblChkFines.getValueAt(rowIndex,4);
				 String loan_id = (String) tblChkFines.getValueAt(rowIndex,0);
				 String paid = (String) tblChkFines.getValueAt(rowIndex,3);
				 
			        try{
			        if(book_returned.equals("NO")){
			        	JOptionPane.showMessageDialog(null, "The book is yet to be returned");
			        }
			           
			        else{
			            if(paid !=null && paid.toString().equals("1")){
			            	
			            	JOptionPane.showMessageDialog(null, "No Dues - Fine already paid");
			            }
			            else{
			            	query="update fines set paid=1 where loan_id='"+loan_id+"'";
			                conn.Connection1(query);
			                JOptionPane.showMessageDialog(null, "Fine paid");
			            }
			                
			        }    
			        }
			        catch (Exception e7) {
			            e7.printStackTrace();
			        }
				 }
			        
			}
		});
		btnPayFine.setBounds(33, 602, 115, 29);
		panel_4.add(btnPayFine);
		
		JButton btnTotalFine = new JButton("Total Fine");
		btnTotalFine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String sum="";
				  boolean show_previous = chckbxShowPreviouslyPaid.isSelected();
				  String card_no = txtFinesCard.getText();
				  Db_Connection conn = new Db_Connection();
			        try{
			        	String query1 ="select * from borrowers where card_id= '"+card_no+"'";
			        	ResultSet rs1= conn.Connection(query1);
			            if(!rs1.next()){
			            	JOptionPane.showMessageDialog(null," Invalid Card ID");
			            }
			            else{
			        	if(show_previous){
			        		String query = "select sum(fine_amt) as sum from fines f join book_loans l on l.loan_id=f.loan_id where card_id ='"+card_no+"' and paid=0";
			        		ResultSet rs=conn.Connection(query);
			        		
			        		if(rs.next()){
			        			sum=rs.getString("sum");
			        			if(sum==null){
			        				JOptionPane.showMessageDialog(null, "No Fines");
			        			}
			        			else{
			        				JOptionPane.showMessageDialog(null, "Total Fines: " + sum);
			        			}
			        		}
			        		
			        		}
			        	else{
			        		String query = "select sum(fine_amt) as sum from fines f join book_loans l on l.loan_id=f.loan_id where f.paid='0' && card_id ='"+card_no+"'";
			        		ResultSet rs=conn.Connection(query);
			        		if(rs.next()){
			        			sum=rs.getString("sum");
			        			if(sum==null){
			        				JOptionPane.showMessageDialog(null, "No Fines");
			        			}
			        			else{
			        				JOptionPane.showMessageDialog(null, "Total Fines: " + sum);
			        			}
			               
			        			
			        		}

			        	}
			            }
			           }
			        catch (Exception e8) {
			            e8.printStackTrace();
			        }
			}

				
			}
		);
		btnTotalFine.setBounds(341, 602, 115, 29);
		panel_4.add(btnTotalFine);
		
		JButton btnRefreshEntries = new JButton("Refresh Entries");
		btnRefreshEntries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				double fine_amount;
				String loan_id, date_in,curdate_diff, paid; 
				Db_Connection conn = new Db_Connection();
				
				String query1 = "select loan_id,date_in,datediff(curdate(), due_date),datediff(date_in,due_date) from book_loans";
				
				String query2, query3, query4;
				
				ResultSet rs1 = conn.Connection(query1);
				try{
					int t=0;
					while(rs1.next())
					{
						
            			loan_id = rs1.getString("loan_id");
            			date_in = rs1.getString("date_in");
            			
            			
            			if(date_in == null||date_in.equals(null) || date_in.equals("") ){
            				System.out.println(t);
            				t++;	
            				curdate_diff =rs1.getString("datediff(curdate(), due_date)");
            				int i = Integer.parseInt(curdate_diff);
            				if(i>0){
            					
            					fine_amount = 0.25*i;
            					
            					query2 = "select * from fines where loan_id='"+loan_id+"'";
            					ResultSet rs2 = conn.Connection(query2);
            					if(rs2.next()){
            						paid = rs2.getString("paid");
            						if(paid !=null && paid.toString().equals("1")){
            							System.out.println("UP to date");
            						}
            						else{
            							
            							query3 = "Update fines set fine_amt = '"+fine_amount +"' where loan_id = '"+loan_id+"'" ;
                						conn.Connection1(query3);
            							
            						}
            					}
            					else{
            					
            						query4 = "insert into fines(loan_id, fine_amt ) values ('"+loan_id+"','"+fine_amount+"')";
            						conn.Connection1(query4);
            					}
            				}
            				else{
            					System.out.println("No fine for :"+ loan_id);
            				}
            			}
            			else{
            				curdate_diff =rs1.getString("datediff(date_in,due_date)");
            				int i = Integer.parseInt(curdate_diff);
            				if(i>0){
            					
            					fine_amount = 0.25*i;
            					
            					query2 = "select * from fines where loan_id='"+loan_id+"'";
            					ResultSet rs2 = conn.Connection(query2);
            					if(rs2.next()){
            						paid = rs2.getString("paid");
            						
            						if(paid !=null && paid.toString().equals("1")){
            							System.out.println("UP to date");
            						}
            						else{
            							
            							query3 = "Update fines set fine_amt = '"+fine_amount +"' where loan_id = '"+loan_id+"'" ;
                						conn.Connection1(query3);
            							
            						}
            					}
            					else{
            					
            						query4 = "insert into fines(loan_id, fine_amt ) values ('"+loan_id+"','"+fine_amount+"')";
            						conn.Connection1(query4);
            					}
            					
            				}
            				else{
            					System.out.println("No fine for :"+ loan_id);
            				}
            			}
            			
					
					}
					JOptionPane.showMessageDialog(null,"Fines data refreshed");
						
				}
				catch(Exception e1) {
					
					e1.printStackTrace();
				}		     
			
		
				
			}
		});
		btnRefreshEntries.setBounds(623, 602, 218, 29);
		panel_4.add(btnRefreshEntries);
		
		
		JButton btnNewButton = new JButton("Get Data");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String card_no= txtFinesCard.getText();
				String isbn,loan_id,fine_amt,paid,book_returned,date_in;
		        boolean show_previous = chckbxShowPreviouslyPaid.isSelected();
		        DefaultTableModel model3 = (DefaultTableModel) tblChkFines.getModel();
		        model3.setRowCount(0);
		        Db_Connection conn = new Db_Connection();
		        try{
		        	
		        	String query,query1;
		        	
		        	 query ="select * from borrowers where card_id= '"+card_no+"'";
		        	
		             query1= "select f.Loan_id, bl.isbn13, f.fine_amt,f.paid,bl.date_in, datediff(curdate(), bl.due_date) as curdue,datediff( bl.date_in,bl.due_date) as indue from book_loans bl join fines f on  bl.Loan_id = f.Loan_id where  bl.card_id='"+card_no+"' having curdue>0 or indue>0;";
		            
		            
		            ResultSet rs = conn.Connection(query1);
		            ResultSet rs1= conn.Connection(query);
		            if(!rs1.next()){
		            	JOptionPane.showMessageDialog(null," Invalid Card ID");
		            }
		            else{
		            	if(!rs.next()){
   
		                	JOptionPane.showMessageDialog(null,"No loans available for the user");
		            	}
		            	else{
		            		do{
		            			isbn = rs.getString("isbn13");
		            			loan_id = rs.getString("loan_id");
		            			
		            			fine_amt = rs.getString("fine_amt");
		            			paid = rs.getString("paid");
		            			date_in = rs.getString("date_in");
		            			
		            			int check_pay = Integer.parseInt(paid);
		            			
		            			if(date_in == null){
		            				book_returned = "NO";
		            			}
		            			else{
		            				book_returned = "YES";
		            			}
		            			if(show_previous){
		            				Object obj[]={loan_id,isbn,fine_amt,paid,book_returned};
	        						model3.addRow(obj);
		            			}
		            			else{
		            				if(check_pay==0){
		            					Object obj[]={loan_id,isbn,fine_amt,paid,book_returned};
		        						model3.addRow(obj);
		            				}
		            			
		            			}
        					
		            		}while(rs.next());
		            	}
		            	
		            }
   
		        }
		        catch (Exception e) {
		            e.printStackTrace();
		        }
			}	
		});
		btnNewButton.setBounds(863, 23, 115, 29);
		panel_4.add(btnNewButton);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(37, 147, 1050, 387);
		panel_4.add(scrollPane_2);
		
		tblChkFines = new JTable();
		tblChkFines.setBounds(37, 136, 1084, 430);
		//panel_4.add(tblChkFines);
		
		
		tblChkFines = new JTable();
		tblChkFines.setModel(new DefaultTableModel
				(
			new Object[][] {
			},
			new String[] {
					"loan_id", "ISBN", "Fine_amt", "paid", "Book_Returned"
			}
						)
				);
		scrollPane_2.setViewportView(tblChkFines);
	}
}
