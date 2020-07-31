package gui;

import world.Game;
import world.entities.item.Coin;
import world.entities.item.Item;
import world.entities.mobs.Player;
import world.entities.mobs.Trader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TraderDialog extends JDialog {

    private JComboBox<String> traderItems = new JComboBox<>();
    private JButton tradeButton;

    private int panelWidth = 300;
    private int panelHeight = 300;

    public TraderDialog(Game game, Player player, Trader trader) {
        super(game.getGui().getFrame(), "Trading", false);

        for(Item item : trader.getItems()) {
            traderItems.addItem(item.getName());
        }
        if(!trader.getItems().isEmpty()) {
            traderItems.setSelectedIndex(0);
        }


        // Find a coin on a player
        List<Item> inven = player.getInventory();
        boolean found = false;
        int coinIndex = 0;
        for(int i = 0; i < inven.size(); i++) {
            if(inven.get(i) instanceof Coin) {
                found = true;
                coinIndex = i;
            }
        }

        tradeButton = new JButton("Trade");
        tradeButton.setEnabled(found);

        final int index = coinIndex;

        tradeButton.addActionListener(e -> {
            player.addInventory(trader.getItems().get(traderItems.getSelectedIndex()));
            player.consumeItem(index);
            trader.removeItem(traderItems.getSelectedIndex());
            this.dispose();
        });

        JPanel tradePanel = new JPanel();
        tradePanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        tradePanel.setSize(new Dimension(panelWidth, panelHeight));
        tradePanel.setLayout(new BoxLayout(tradePanel, BoxLayout.Y_AXIS));

        tradePanel.add(traderItems);
        tradePanel.add(tradeButton);

        add(tradePanel);
        pack();
        setLocationRelativeTo(game.getGui().getFrame());
        setVisible(true);
    }
}
