# Follow the Money app
This is an Android application written in Java for tracking a user's expenses.
<br>
<div align="center">
  <img src="https://github.com/roeishc/follow-the-money-app/assets/95538414/3a94301c-28e9-4afb-baab-bde74477be74" width="25%">
</div>
<br>
The app allows adding expenses in multiple categories, edit, or delete them.
<br>
<br>
Additional functionalities include:<br><br>
<table>
  <tr>
    <td width="25%">Viewing all expenses in a scrollable list</td>
    <td width="25%">A summary of all expenses</td>
    <td width="25%">Sharing your total via SMS</td>
    <td width="25%">And maybe learn a little about why we make bad financial decisions 😀</td>
  </tr>
  <tr>
    <td align="center"><img src="https://github.com/roeishc/follow-the-money-app/assets/95538414/79e4ad1a-fa9c-4102-824d-b4ab0b4c8bce" width="200"></td>
    <td align="center"><img src="https://github.com/roeishc/follow-the-money-app/assets/95538414/ed5ef6ad-3178-4838-a3b8-2dabd4fa691f" width="200"></td>
    <td align="center"><img src="https://github.com/roeishc/follow-the-money-app/assets/95538414/d40ef5c1-563a-4c22-86c4-73ff6f39e999" width="200"></td>
    <td align="center"><img src="https://github.com/roeishc/follow-the-money-app/assets/95538414/ff06315c-6977-40a0-bd5a-b422daa9fe49" width="200"></td>
  </tr>
</table>
<br>

### A note about the data
The data is saved in Firebase Realtime Database (JSON). For the sake of keeping the database both intact and safe, I disabled the write-permissions in Firebase. You can still install the app on your Android phone and see all the expenses I added (for the sake of development) if you wish to experiment yourself! In addition, I thoroughly tested the different scenarios of adding/updating/deleting expenses so that each action will be reflected across all activities. However, since limiting the write-permissions, there might be some peculiar behavior, such as an update of an expense being reflected in only some activities, and not all. Lastly, all changes you make will be reflected in the same session, and only locally on your device. The next time you open the app, the data will be refreshed anew.

