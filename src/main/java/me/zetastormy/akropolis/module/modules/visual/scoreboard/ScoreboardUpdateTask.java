/*
 * This file is part of Akropolis
 *
 * Copyright (c) 2024 DevBlook Team and others
 *
 * Akropolis free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Akropolis is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Akropolis. If not, see <http://www.gnu.org/licenses/>.
 */

package me.zetastormy.akropolis.module.modules.visual.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScoreboardUpdateTask implements Runnable {
    private final ScoreboardManager scoreboardManager;

    public ScoreboardUpdateTask(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public void run() {
        List<UUID> toRemove = new ArrayList<>();

        scoreboardManager.getPlayers().forEach(uuid -> {
            if (scoreboardManager.updateScoreboard(uuid) == null)
                toRemove.add(uuid);
        });

        scoreboardManager.getPlayers().removeAll(toRemove);
    }
}
