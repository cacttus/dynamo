package dynamo;

public class GameScript {
	
	public static String setStory() {
		String story = 	"In the distant future, an enormous population increase on \nthe planet earth led to the starvation and hunger of millio-\nns of people, and in this, an effort was put in place by th-\ne United Nations, to build out into the depths of space, to\n provide room for the starved and depraved people that need-\ned homes, and to expand the land available for agriculture,\n livestock, and construction.  "+
"At the time, nobody had rea-\nlly been building out into space except for scientists and \nspace-tourism agencies.  Since there was no widespread acco-\nmmodation for agriculture, the people turned to other plane-\nts for their needs.  IO, Europa, and Ganymede, moons of the\n planet Jupiter, and the planets Mars and Venus were modifi-\ned by scientists to support agriculture, people, and techno-\nlogy; very similar to Earth.  To spur the travel to other p-\nlaces, the United States re-enacted the Homestead Act, whic-\nh allowed individuals to claim distant land for agriculture-\n, and eventually receive the land as their own.  "+
"The numbe-\nr of farms and plantations grew enormously, and with this, \ncame mini-malls, bars, casinos and other public places.  Ev-\nentually, towns grew into cities, and cities grew into coun-\ntries.  In all the rush of leaving Earth, there was still a\n large problem at hand, which was that with the current tec-\nhnology, it could take up to four months to travel from one\n planet to the next.  Hyperspace gates were created as spac-\ne-ship travel routes between the new planets.  With this te-\nchnology specially designed spacecraft, traveling through t-\nhe gates could go from one planet to the next, in under thr-\nee seconds.  "+
"In the great migration of billions of familie-\ns leaving Earth, the political and lawful presence of plane-\nt diminished.  Earth became a center point for criminals, w-\nho themselves, took their business out into space.  Since h-\nyperspace gates were like a tunnel and could not be seen fr-\nom the outside, the criminals took their business and deals\n to the inside of the gates."+
	"Space was largely unregulated\n at the time that the gates were put in place, so in a join-\nt effort to protect the people.  A space police and investi-\ngation force was established to monitor and control all ill-\negal activities that go in and around civilian travel route-\ns.  This new force, named the sr-10 established themselves \nwell throughout space, and focused a large portion of their\n efforts, to the area of hyperspace where the criminals wer-\ne."+
	"Many years later, hyperspace gates had been in use for \nsome time, and showed visible signs of wear and damage.  If\n the gates were to malfunction they could have potentially \ncollapsed, killing many thousands of people inside.  Offici-\nals decided it was time to replace these gates with newer a-\nnd better technology, however, here was one problem, engine-\ners could not think of a way to take them down, without des-\ntroying space completely.  The gates themselves had to be c-\nompletely destroyed, and all at the same time.  An idea was\n put in place to create a series of machines that were larg-\ne and powerful, and could destroy the gates.  So, with the \ncollaboration of scientists and engineers,  a space-ship wa-\ns created that could destroy the dimensional gates using a \nconcentrated beam of gravitational energy.  Engineers nickn-\named the machine Dynamo.  "+
"The Dynamo was an enormous machi-\nne, twelve football fields in size, and weighing as much as\n a small planet.  It is black in color, with green lights o-\nn its wings radiating nuclear energy from the core that dri-\nves the machine.  On the front was a long narrow pole wrapp-\ned in various cords, which, when charged with energy, allow-\ns the machine to shoot a laser beam destroying anything in \nits path."+
	"The creation of the Dynamo was heard all through-\nout the solar system.  There was much controversy about how\n destructive the machine actually was, and the implications\n it would have if it fell into the wrong hands.  Upon heari-\nng about this, an established Russian mob, called the Nozh \nthought that possessing the Dynamo would make anyone who ow-\nns it the most powerful person in the solar system.  Thus, \nthe Nozh broke into the area where the dynamo was being bui-\nlt, and took it back to their headquarters, the whereabouts\n of which, is unknown."+
	"A team of the sr-10 task force has \nbeen assigned to investigate, and find the Dynamo and retri-\neve it."+
		     "This is their story... ";
		return story;
	}
	
	public static String[] setScript( int numLevels ) {
		// the ';' character is a new line.
		String[] g_DisplayStrings = new String[numLevels];
		 
		// - Characters
		// Roger - Main character
		// Jeff - Roger's friend
		// Colnel Walker - THeir boss
		// Kelly - Roger's friend
		
		 g_DisplayStrings[0] = ";";
		// "Roger and Jeff are long time vetrans in the space police force SR-10.  During a normal day, Jeff gives roger an unusual call on the radio."+
        // ";Roger: Clanking thing.."+
        // ";>BZZZT<"+
        // ";>Radio< : Hey Roger, it's Jeff."+
        // ";Roger: What's up Jeff?"+
        // ";>Jeff<: Colnel Walker called in from Ganymede corona base 166-F, D-Block.  He reported a bunch of gang members driving unlicensed weaponcraft."+
        // "  I want you to go and\ncheck it out.\""+
        // "\nRoger: D-Block?...Alone? I'm getting too old for this."+
        // "\n>Jeff<: Don't worry Roger, i'll call some backup in for you, I'm also coming too.\""+
        // "\nRoger: \"Thanks Jeff. I'm On my way.\""+
        // "\n>Jeff<: \"Be careful.  Over and out.\""+
        // "\nRoger: \"(...D-Block...hmm...)\"";
		
        g_DisplayStrings[1] = ";";
		// ">Jeff< :\"Dang! That little Bugger\nescaped!\""+
        // "\nRoger: \"What's wrong Jeff?\""+
        // "\n>Jeff<: \"The gang leader slipped out\n during the fight.\""+
        // " \nRoger: \"I'll head down there and nab him.\""+
        // "\n>Jeff<: \"Good idea Roger, just don't get hurt,\nthat ship you're driving is worth a "+
        // "\n ton.\""+
        // "\nRoger: \"Right...\""+
        // "\n>Jeff<: \"Just kidding. Hey if he gives you\ntoo much trouble, go ahead and\nbeep Sergent Thomson."+
        // "\n  I'm going back to HQ for repairs.\n Over.\""+
        // "\nGANYMEDE\n";
		
        g_DisplayStrings[2] = ";";
		// "Roger: \"Stop right now!\""+
        // "\n>Gang Leader< \"What? Get lost!\""+
        // "\nRoger: \"Pull to the side and face\n the criminal justice system.\""+
        // "\n>Gang Leader< \"What! You daren't mess with me!"+
        // "\n>Gang Leader< I am a member of the Red Ts!\"\nRoger: \"Shut up and pull over\""+
        // "\n>Gang Leader< Leave me be cop!, There is about to be a revolt"+
        // "\n,you and other government people \nalike will meet their doom.\""+
        // "\nRoger: \"Alright guy I think you're done.\""+
        // "\n<<ZAP>>"+
        // "\n>Gang Leader< \"Arrrrggghhhh!!!!\""+
        // "\nRoger: \"Hey Jeff, i disposed of the leader.\""+
        // "\n>Jeff<: \"Good work Roger.\""+
        // "\nRoger: \"He mentioned some sort of Red Ts revolt, any ideas?\""+
        // "\n>Jeff<: \"hmm... mabe...\""+
        // "\n>Jeff<: \"There used to be a rumor that\n they are building some sort of weapon."+
        // "\n>Jeff<: \"... Roger, go to Hyzer on Titan, see what you can uncover.\nRoger: \"Alright.\""+
        // "\nTITAN";
		
        g_DisplayStrings[3] = ";";
		// ">Jeff<: \"Ugh!>< Burp ><><...\""+
        // "\nRoger: \"Jeff? hey..?\""+
        // "\n>Jeff<: \"..Yeaaah....\""+
        // "\nRoger: \"Jeff, are you alright?\""+
        // "\n>Jeff<: \"..Yeahhah hahaha ha\""+
        // "\nRoger: \"Well.. I managed to recover\nsome information.  It turns out that\nthe machine is called the Dynamo."+
        // "\n>Jeff<: \"(Hicup) Dynamo? dang.\"\nRoger: \"Yes, somebody mentioned IO\nso i'm headed out there.\""+
        // "\n>Jeff<: \"Alright Roger, you be a good\nboy now.\""+
        // "\nRoger: \"I'll do my best sir.\"\nRoger: \"Over.\""+
        // "\n>Jeff<: \"Ooooverrrrrr...\""+
        // "\nRoger: (Gee..)\n";
		
        g_DisplayStrings[4] =  ";";
		// ">Jeff<: \"Roger, did you get anything?\""+
        // "\nRoger: \"Yes, there is supposed to be a group of rebels\nstationed on earth.\""+
        // "\n>Jeff<: \"Earth, dangerous.\""+
        // "\n>Jeff<: \"I'll send agend Kelly Marie along with you\""+
        // "\nRoger: \"That newbie\""+
        // "\n>Jeff<: \"Now, she should be very helpful if you get\ninto a situation, you know that\""+
        // "\nRoger: \"Right.\""+
        // "\n>Jeff<: \"Anyways, if not, she would make for a good\nmeat shield.\""+
        // "\nRoger: \"Okay Jeff\""+
        // "\n>Jeff<: \"Over\"\nRoger: \"Over\"\n";
		
        g_DisplayStrings[5] =  ";";
		// ">Kelly<: \"Whew there was a lot of them.\""+
        // "\nRoger: \"You bet.\""+
        // "\n\nRoger: \"Did you get anything?\""+
        // "\n>Kelly<: \"Yes I overheard some thugs talk about\nthe Dynamo.\""+
        // "\n>Kelly<: \"One said it was powerful enough\n to destroy planets.\""+
        // "\nRoger: \"Wow, Did you get any leads?\""+
        // "\n>Kelly<: \"Not of the ship, but one did mention the\nwarship bay area on the moon.\""+
        // "\nRoger: \"We should probably head over there then.\""+
        // "\n>Kelly<: \"Good idea, let's go!\""+
        // "\n";
		
        g_DisplayStrings[6] =  ";";
		// ">Kelly< \"This gate jumps straight through IO.\""+
        // "\n>Kelly<: \"This would be the perfect place\nto hide something.\""+
        // "\nRoger: \"This must be where they are keeping the Dynamo.\""+
        // "\nRoger: \"Alright Kelly I'll go on ahead.\""+
        // "\n>Kelly<: \"Be careful.\""+
        // "\nRoger: \"You go back to the station and process these criminals.\""+
        // "\n>Kelly<: \"Alright\""+
        // "\n";
		
        g_DisplayStrings[7] =  ";";//">Dynamo<: BEGONE INTRUDER";
	
		
		
		
		return g_DisplayStrings;
	}
}